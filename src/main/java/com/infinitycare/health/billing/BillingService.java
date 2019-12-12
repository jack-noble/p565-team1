package com.infinitycare.health.billing;

import com.infinitycare.health.database.*;
import com.infinitycare.health.login.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class BillingService extends ServiceUtility {

    @Autowired
    public PatientRepository patientRepository;

    @Autowired
    public DoctorRepository doctorRepository;

    @Autowired
    public IpPlanRepository ipPlanRepository;

    @Autowired
    public IpRepository ipRepository;

    @Autowired
    public AppointmentsRepository appointmentsRepository;

    public BillingService(PatientRepository patientRepository, DoctorRepository doctorRepository, IpPlanRepository ipPlanRepository, IpRepository ipRepository, AppointmentsRepository appointmentRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.ipPlanRepository = ipPlanRepository;
        this.ipRepository = ipRepository;
        this.appointmentsRepository = appointmentRepository;
    }

    public void getPatientsUnpaidBills(Map<String, Object> result, String username) {
        // When there's no insurance plan in the AppointmentDetails(old data), just use the patients current insurance plan
        // Handle usecases where the patient does not have any insurance

        List<AppointmentsDetails> appointmentsList = appointmentsRepository.findAllPatientAppointments(username);
        List<Bill> billsToBePaid = new ArrayList<>();
        // int totalOutOfPocketAmount = 0;
        // int totalAmountCoveredByInsurance = 0;
        for (AppointmentsDetails appointment : appointmentsList) {
            if (!appointment.isBillPaidByPatient()) {
                Bill bill;
                int doctorPrice = doctorRepository.findById(Integer.toString(appointment.getDoctorUsername().hashCode())).get().getConsultationFee();
                String insurancePlan = appointment.getInsurancePlan();
                if (StringUtils.isEmpty(insurancePlan)) {
                    PatientDetails patientDetails = patientRepository.findById(Integer.toString(username.hashCode())).get();
                    insurancePlan = patientDetails.getInsurancePlan();
                }

                Optional<IpPlanDetails> plan = ipPlanRepository.findById(Integer.toString(insurancePlan.hashCode()));
                if (StringUtils.isEmpty(insurancePlan) || !plan.isPresent()) {
                    bill = new Bill(appointment.getDoctorDisplayName(), appointment.getDoctorUsername(), appointment.getReason(), appointment.getDisplayDate(), doctorPrice, appointment.getId(), appointment.getPatientName(), appointment.getPatientUsername());                    
                    // totalOutOfPocketAmount = totalOutOfPocketAmount + doctorPrice;
                } else if (appointment.getInsuranceProviderBillStatus().equals(Bill.DENIED)) {
                    // If the bill is denied, then the whole price needs to be paid by the patient
                    bill = new Bill(appointment.getDoctorDisplayName(), appointment.getDoctorUsername(), appointment.getReason(), appointment.getDisplayDate(), doctorPrice, appointment.getId(), appointment.getPatientName(), appointment.getPatientUsername());                    
                    // totalOutOfPocketAmount = totalOutOfPocketAmount + doctorPrice;
                } else {
                    bill = new Bill(appointment.getDoctorDisplayName(), appointment.getDoctorUsername(), appointment.getReason(), appointment.getDisplayDate(), Integer.parseInt(plan.get().getCoPayment()), appointment.getId(), appointment.getPatientName(), appointment.getPatientUsername());
                    // totalOutOfPocketAmount = totalOutOfPocketAmount + Integer.parseInt(plan.get().getCoPayment());
                    // totalAmountCoveredByInsurance += doctorPrice - Integer.parseInt(plan.get().getCoPayment());
                }
                billsToBePaid.add(bill);
            }
        }

        result.put("billsToBePaid", billsToBePaid);
        // result.put("totalOutOfPocketAmount", totalOutOfPocketAmount);
        populateStatsForPatient(result, username);
    }

    private void populateStatsForPatient(Map<String, Object> result, String username) {
        int totalInProcessAmountByInsurance = 0;
        int totalAmountCoveredByInsurance = 0;
        int totalAmountDeniedByInsurance = 0;
        int totalAmountOfAllBills = 0;
        int outOfPocketAmountSpent = 0;
        int outOfPocketLimit = 0;

        List<AppointmentsDetails> appointmentsList = appointmentsRepository.findAllPatientAppointments(username);
        for (AppointmentsDetails appointment : appointmentsList) {
            int doctorPrice = doctorRepository.findById(Integer.toString(appointment.getDoctorUsername().hashCode())).get().getConsultationFee();
            String insurancePlan = appointment.getInsurancePlan();
            if (StringUtils.isEmpty(insurancePlan)) {
                PatientDetails patientDetails = patientRepository.findById(Integer.toString(username.hashCode())).get();
                insurancePlan = patientDetails.getInsurancePlan();
            }

            Optional<IpPlanDetails> plan = ipPlanRepository.findById(Integer.toString(insurancePlan.hashCode()));
            outOfPocketLimit = Integer.parseInt(plan.get().getAnnualOutOfPocketLimit());
            if (StringUtils.isEmpty(insurancePlan) || !plan.isPresent()) {
                //
            } else if (appointment.getInsuranceProviderBillStatus().equals(Bill.DENIED)) {
                totalAmountDeniedByInsurance = totalAmountDeniedByInsurance + doctorPrice - Integer.parseInt(plan.get().getCoPayment());
                outOfPocketAmountSpent = outOfPocketAmountSpent + doctorPrice;
            } else if(appointment.getInsuranceProviderBillStatus().equals(Bill.IN_PROCESS)){
                totalInProcessAmountByInsurance = totalInProcessAmountByInsurance + doctorPrice - Integer.parseInt(plan.get().getCoPayment());
                outOfPocketAmountSpent = outOfPocketAmountSpent + Integer.parseInt(plan.get().getCoPayment());
            } else if(appointment.getInsuranceProviderBillStatus().equals(Bill.APPROVED)) {
                totalAmountCoveredByInsurance = totalAmountCoveredByInsurance + doctorPrice - Integer.parseInt(plan.get().getCoPayment());
                outOfPocketAmountSpent = outOfPocketAmountSpent + Integer.parseInt(plan.get().getCoPayment());
            }
        }

        result.put("totalAmountCoveredByInsurance", totalAmountCoveredByInsurance);
        result.put("totalAmountDeniedByInsurance", totalAmountDeniedByInsurance);
        result.put("totalInProcessAmountByInsurance", totalInProcessAmountByInsurance);
        result.put("totalAmountCoveredByPatient", outOfPocketAmountSpent);
        result.put("totalAmountOfAllBills", totalAmountOfAllBills);
        result.put("outOfPocketAmountSpent", outOfPocketAmountSpent);
        result.put("outOfPocketLimit", outOfPocketLimit);
        result.put("outOfPocketLimitRemaining", outOfPocketLimit - outOfPocketAmountSpent);
    }

    public ResponseEntity<?> getPatientPaidBills(HttpServletRequest request) {
        // When there's no insurance plan in the AppointmentDetails(old data), just use the patients current insurance plan
        // Handle usecases where the patient does not have any insurance

        String username = getUsername(request);
        List<AppointmentsDetails> appointmentsList = appointmentsRepository.findAllPatientAppointments(username);

        List<Bill> billsPaid = new ArrayList<>();
        appointmentsList.forEach(appointment -> {
            if(appointment.isBillPaidByPatient()) {
                Bill bill;
                int doctorPrice = doctorRepository.findById(Integer.toString(appointment.getDoctorUsername().hashCode())).get().getConsultationFee();
                
                bill = new Bill(appointment.getDoctorDisplayName(), appointment.getDoctorUsername(), appointment.getReason(), appointment.getDisplayDate(), doctorPrice, appointment.getId(), appointment.getPatientName(), appointment.getPatientUsername());
                billsPaid.add(bill);
                
                // String insurancePlan = appointment.getInsurancePlan();
                // if (StringUtils.isEmpty(insurancePlan)) {
                //     PatientDetails patientDetails = patientRepository.findById(Integer.toString(username.hashCode())).get();
                //     insurancePlan = patientDetails.getInsurancePlan();
                // }

                // Optional<IpPlanDetails> plan = ipPlanRepository.findById(Integer.toString(insurancePlan.hashCode()));
                // if (StringUtils.isEmpty(insurancePlan) || !plan.isPresent()) {
                //     bill = new Bill(appointment.getDoctorDisplayName(), appointment.getReason(), appointment.getDisplayDate(), doctorPrice, appointment.getId());
                // } else if(appointment.getInsuranceProviderBillStatus().equals(Bill.DENIED)) {
                //     // If the bill is denied, then the whole price needs to be paid by the patient
                //     bill = new Bill(appointment.getDoctorDisplayName(), appointment.getReason(), appointment.getDisplayDate(), doctorPrice, appointment.getId());
                // } else {
                //     bill = new Bill(appointment.getDoctorDisplayName(), appointment.getReason(), appointment.getDisplayDate(), Integer.parseInt(plan.get().getCoPayment()), appointment.getId());
                // }
                // billsToBePaid.add(bill);
            }
        });

        return ResponseEntity.ok(billsPaid);
    }

    public ResponseEntity<?> getAllPatientClaims(HttpServletRequest request) {
        // When there's no insurance plan in the AppointmentDetails(old data), just use the patients current insurance plan
        // Handle usecases where the patient does not have any insurance

        String username = getUsername(request);
        Map<String, Object> result = new HashMap<>();

        List<AppointmentsDetails> appointmentsList = appointmentsRepository.findAllPatientAppointments(username);

        List<Bill> approvedClaims = new ArrayList<>();
        List<Bill> inProcessClaims = new ArrayList<>();
        List<Bill> deniedClaims = new ArrayList<>();

        appointmentsList.forEach(appointment -> {
            Bill bill = null;
            int doctorPrice = doctorRepository.findById(Integer.toString(appointment.getDoctorUsername().hashCode())).get().getConsultationFee();
            String insurancePlan = appointment.getInsurancePlan();
            if (StringUtils.isEmpty(insurancePlan)) {
                PatientDetails patientDetails = patientRepository.findById(Integer.toString(username.hashCode())).get();
                insurancePlan = patientDetails.getInsurancePlan();
            }

            Optional<IpPlanDetails> plan = ipPlanRepository.findById(Integer.toString(insurancePlan.hashCode()));
            if (plan.isPresent()) {
                bill = new Bill(appointment.getDoctorDisplayName(), appointment.getDoctorUsername(), appointment.getReason(), appointment.getDisplayDate(), doctorPrice, appointment.getId(), appointment.getPatientName(), appointment.getPatientUsername());
            }

            if(Bill.APPROVED.equals(appointment.getInsuranceProviderBillStatus()))
                approvedClaims.add(bill);
            else if(Bill.IN_PROCESS.equals(appointment.getInsuranceProviderBillStatus()))
                inProcessClaims.add(bill);
            else if(Bill.DENIED.equals(appointment.getInsuranceProviderBillStatus()))
                deniedClaims.add(bill);
        });

        result.put("approvedClaims", approvedClaims);
        result.put("inProcessClaims", inProcessClaims);
        result.put("deniedClaims", deniedClaims);
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> editPatientBillStatus(HttpServletRequest request, boolean didPatientPay) {
        Map<String, String> postBody = getPostBodyInAMap(request);
        String appointmentId = postBody.get("id");

        Optional<AppointmentsDetails> appointment = appointmentsRepository.findById(appointmentId);
        if(appointment.isPresent()) {
            appointment.get().setBillPaidByPatient(didPatientPay);
            appointmentsRepository.save(appointment.get());
        }

        return ResponseEntity.ok(didPatientPay + appointmentId);
    }

    public ResponseEntity<?> editInsuranceProviderBillStatus(HttpServletRequest request, String billStatus) {
        Map<String, String> postBody = getPostBodyInAMap(request);
        String appointmentId = postBody.get("id");

        Optional<AppointmentsDetails> appointment = appointmentsRepository.findById(appointmentId);
        if(appointment.isPresent()) {
            appointment.get().setInsuranceProviderBillStatus(billStatus);
            appointmentsRepository.save(appointment.get());
        }

        return ResponseEntity.ok(billStatus + appointmentId);
    }

    public ResponseEntity<?> getInsuranceProviderClaims(HttpServletRequest request) {
        // Denied Bills are displayed both for the insurance provider and patient.
        String username = getUsername(request);
        List<AppointmentsDetails> appointmentsList = appointmentsRepository.findAll();

        Map<String, Object> results = new HashMap<>();
        List<Bill> billsToBePaid = new ArrayList<>();
        List<Bill> approvedBills = new ArrayList<>();
        List<Bill> deniedBills = new ArrayList<>();

        int[] amountPaidByInsuranceProvider = new int[0];
        amountPaidByInsuranceProvider[0] = 0;

        int[] amountPaidByPatientsOfTheInsuranceProvider = new int[0];
        amountPaidByPatientsOfTheInsuranceProvider[0] = 0;

        Optional<IPDetails> ip = ipRepository.findById(Integer.toString(username.hashCode()));
        if(ip.isPresent()) {
            IPDetails insuranceProvider = ip.get();
            appointmentsList.forEach(appointment -> {
                int doctorPrice = doctorRepository.findById(Integer.toString(appointment.getDoctorUsername().hashCode())).get().getConsultationFee();
                String insurancePlan = appointment.getInsurancePlan();
                if (StringUtils.isEmpty(insurancePlan)) {
                    insurancePlan = patientRepository.findById(Integer.toString(appointment.mPatientUsername.hashCode())).get().getInsurancePlan();
                }

                Optional<IpPlanDetails> plan = ipPlanRepository.findById(Integer.toString(insurancePlan.hashCode()));
                if(insuranceProvider.getIpPlans().contains(insurancePlan) && plan.isPresent()) {
                    amountPaidByInsuranceProvider[0] += doctorPrice - Integer.parseInt(plan.get().getCoPayment());
                    amountPaidByPatientsOfTheInsuranceProvider[0] += Integer.parseInt(plan.get().getCoPayment());
                    Bill bill = new Bill(appointment.getDoctorDisplayName(), appointment.getDoctorUsername(), appointment.getReason(), appointment.getDisplayDate(), doctorPrice, appointment.getId(), appointment.getPatientName(), appointment.getPatientUsername());

                    if(appointment.getInsuranceProviderBillStatus().equals(Bill.IN_PROCESS))
                        billsToBePaid.add(bill);
                    else if(appointment.getInsuranceProviderBillStatus().equals(Bill.APPROVED))
                        approvedBills.add(bill);
                    else if(appointment.getInsuranceProviderBillStatus().equals(Bill.DENIED))
                        deniedBills.add(bill);
                }
            });
        }

        results.put("billsToBePaid", billsToBePaid);
        results.put("approvedBills", approvedBills);
        results.put("deniedBills", deniedBills);
        results.put("amountPaidByInsuranceProvider", amountPaidByInsuranceProvider[0]);
        results.put("amountPaidByPatientsOfTheInsuranceProvider", amountPaidByPatientsOfTheInsuranceProvider[0]);
        return ResponseEntity.ok(results);
    }

}
