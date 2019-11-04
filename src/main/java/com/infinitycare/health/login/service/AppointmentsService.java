package com.infinitycare.health.login.service;

import com.infinitycare.health.database.AppointmentsRepository;
import com.infinitycare.health.database.DoctorRepository;
import com.infinitycare.health.database.PatientRepository;
import com.infinitycare.health.login.SendEmailSMTP;
import com.infinitycare.health.login.model.AppointmentsDetails;
import com.infinitycare.health.login.model.ServiceUtility;
import com.infinitycare.health.login.model.DoctorDetails;
import com.infinitycare.health.login.model.PatientDetails;
import com.mongodb.BasicDBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AppointmentsService extends ServiceUtility {

    @Autowired
    public PatientRepository patientRepository;

    @Autowired
    public DoctorRepository doctorRepository;

    @Autowired
    public AppointmentsRepository appointmentsRepository;

    public AppointmentsService(PatientRepository patientRepository, DoctorRepository doctorRepository, AppointmentsRepository appointmentsRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentsRepository = appointmentsRepository;
    }

    public ResponseEntity<?> getTimeSlots(HttpServletRequest request, String doctorusername) {
        String date = getPostBodyInAMap(request).get("date"); // Date format: mm/dd/yyyy

        // 0:9, 1:10, 2:11, 3:12, 4:1, 5:2, 6:3, 7:4.
        List<Integer> availableTimeSlots = new ArrayList<>();
        Collections.addAll(availableTimeSlots, 0, 1, 2, 3, 4, 5, 6, 7);

        Map<String, Object> result = new HashMap<>();
        List<Integer> timeSlotIds = new ArrayList<>();
        List<String> finalts = new ArrayList<>();

        Optional<DoctorDetails> doctorQueriedFromDB = doctorRepository.findById(Integer.toString(doctorusername.hashCode()));

        if(doctorQueriedFromDB.isPresent()) {
            ArrayList timeSlots = doctorQueriedFromDB.get().mTimeSlots;
            for (Object timeSlot : timeSlots) {
                BasicDBObject ts = new BasicDBObject((LinkedHashMap) timeSlot);
                if (ts.get("date").equals(date)) {
                    timeSlotIds = (List<Integer>) ts.get("ts");
                }
            }

            if (timeSlotIds == null && timeSlotIds.isEmpty()) { timeSlotIds = availableTimeSlots; }
            else {
                for (Integer availableTimeSlot : availableTimeSlots) {
                    if (!timeSlotIds.contains(availableTimeSlot)) { finalts.add((9 + availableTimeSlot) + ":00"); }
                }
            }
        }

        result.put("TimeSlots", finalts);
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> createAppointments(HttpServletRequest request) {
        String username = getUsername(request);
        String doctorusername = getPostBodyInAMap(request).get("doctorusername");
        String time = getPostBodyInAMap(request).get("time");
        String datestring = getPostBodyInAMap(request).get("date");

        Date date = new Date();
        boolean isAppointmentCreated = false;
        AppointmentsDetails appointmentsDetails = null;
        Map<String, Object> result = new HashMap<>();

        DateFormat inFormat = new SimpleDateFormat( "MM/dd/yyyy");

        try { date = inFormat.parse(datestring); }
        catch ( ParseException e ) { e.printStackTrace(); }

        date.setHours(Integer.parseInt(time.split(":")[0]));

        Optional<DoctorDetails> doctorQueriedFromDB = doctorRepository.findById(Integer.toString(doctorusername.hashCode()));
        Optional<PatientDetails> patientQueriedFromDB = patientRepository.findById(Integer.toString(username.hashCode()));

        if(doctorQueriedFromDB.isPresent() && patientQueriedFromDB.isPresent()) {
            appointmentsDetails = new AppointmentsDetails(username, doctorusername, date, doctorQueriedFromDB.get().mHospital,
                    doctorQueriedFromDB.get().mAddress, (patientQueriedFromDB.get().mFirstName + " " + patientQueriedFromDB.get().mLastName),
                    (doctorQueriedFromDB.get().mFirstName + " " + doctorQueriedFromDB.get().mLastName));
            appointmentsRepository.save(appointmentsDetails);
            isAppointmentCreated = true;

            DoctorDetails doctorDetails = doctorQueriedFromDB.get();
            ArrayList timeSlots = doctorQueriedFromDB.get().mTimeSlots;
            List<Integer> timeSlotIds = new ArrayList<>();
            BasicDBObject newTimeSlot = new BasicDBObject();
            boolean isDateFound = false;

            for (Object timeSlot : timeSlots) {
                BasicDBObject ts = new BasicDBObject((LinkedHashMap) timeSlot);
                if (ts.get("date").equals(datestring)) {
                    newTimeSlot = ts;
                    timeSlotIds = (List<Integer>) ts.get("ts");
                    timeSlotIds.add(date.getHours() - 9);
                    ((LinkedHashMap) timeSlot).replace("ts", timeSlotIds);
                    isDateFound = true;
                    break;
                }
            }

            if(!isDateFound) {
                newTimeSlot.put("date", datestring);
                timeSlotIds.add(date.getHours() - 9);
                newTimeSlot.put("ts", timeSlotIds);
                timeSlots.add(newTimeSlot);
            }

            doctorDetails.setTimeSlots(timeSlots);
            doctorRepository.save(doctorDetails);
        }

        String emailBody = "<h1>" + "InfinityCare" + "</h1>\n\n" +"<h2>" + "Your Appointment is Confirmed with "
                + appointmentsDetails.mDoctorName + "</h2>\n" + "<h3>" + "When: " + appointmentsDetails.mDate + "</h3>\n"
                + "<h3>" + "Where: " + appointmentsDetails.mHospital + " "+ appointmentsDetails.mLocation + "</h3>";

        SendEmailSMTP.sendFromGMail(new String[]{username, doctorusername}, "Appointment Confirmed", emailBody);

        result.put("isAppointmentCreated", isAppointmentCreated);
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> getAppointments(HttpServletRequest request, String userType) {
        // doing a sanity check when sending appointments to the user.
        Map<String, Object> results = new HashMap();
        String username = getUsername(request);
        //String username = request.getParameter(USERNAME);

        List<AppointmentsDetails> appointmentsList = null;
        Date now = new Date();

        if(userType.equals(PATIENT)) {
            appointmentsList = appointmentsRepository.findAllPatientAppointments(username);
            for (int i = 0; i < appointmentsList.size(); i++) {
                if(now.compareTo(appointmentsList.get(i).mDate) > 0) {
                    appointmentsList.get(i).setStatus(false);
                    appointmentsRepository.save(appointmentsList.get(i));
                    appointmentsList.remove(appointmentsList.get(i));
                }
            }
        }

        if(userType.equals(DOCTOR)) {
            appointmentsList = appointmentsRepository.findAllDoctorAppointments(username);
            for (int i = 0; i < appointmentsList.size(); i++) {
                if(now.compareTo(appointmentsList.get(i).mDate) > 0) {
                    appointmentsList.get(i).setStatus(false);
                    appointmentsRepository.save(appointmentsList.get(i));
                    appointmentsList.remove(appointmentsList.get(i));
                }
            }
        }

        results.put("CurrentAppointments", appointmentsList);
        List<AppointmentsDetails> appointmentsDetails = getPastAppointmentsAsList(request, userType);
        if (appointmentsDetails == null || appointmentsDetails.isEmpty()) {
            results.put("PastAppointments", new ArrayList<AppointmentsDetails>());
        } else {
            results.put("PastAppointments", appointmentsDetails);
        }

        return ResponseEntity.ok(results);
    }

    public ResponseEntity<?> getPastAppointments(HttpServletRequest request, String userType) {
        Map<String, Object> result = new HashMap();

        result.put("Appointments", getPastAppointmentsAsList(request, userType));
        return ResponseEntity.ok(result);
    }

    private List<AppointmentsDetails> getPastAppointmentsAsList(HttpServletRequest request, String userType) {
        String username = getUsername(request);
        //String username = request.getParameter(USERNAME);

        List<AppointmentsDetails> result = new ArrayList();

        if(userType.equals(PATIENT)) {
            List<AppointmentsDetails> appointmentsList = appointmentsRepository.findAllPatientAppointments(username);
            for (AppointmentsDetails appointmentsDetails : appointmentsList) {
                if (!appointmentsDetails.getStatus()) {
                    result.add(appointmentsDetails);
                }
            }
        }

        if(userType.equals(DOCTOR)) {
            List<AppointmentsDetails> appointmentsList = appointmentsRepository.findAllDoctorAppointments(username);
            for (AppointmentsDetails appointmentsDetails : appointmentsList) {
                if (!appointmentsDetails.getStatus()) {
                    result.add(appointmentsDetails);
                }
            }
        }

        return result;
    }

    public ResponseEntity<?> cancelAppointments(HttpServletRequest request, String userType) {
        String id = getPostBodyInAMap(request).get("id");

        boolean isAppointmentDeleted = false;
        Map<String, Object> result = new HashMap<>();

        if(userType.equals(PATIENT) || userType.equals(DOCTOR)) {
            Optional<AppointmentsDetails> appt = appointmentsRepository.findById(id);
            if(appt.isPresent()) {
                appointmentsRepository.deleteById(id);
                isAppointmentDeleted = true;

                String emailBody = "<h1>" + "InfinityCare" + "</h1>\n\n" +"<h2>" + "Your Appointment with "
                        + appt.get().mDoctorName + "has been cancelled." + "</h2>\n" + "<h3>" + "When: " + appt.get().mDate + "</h3>\n"
                        + "<h3>" + "Where: " + appt.get().mHospital + " "+ appt.get().mLocation + "</h3>";

                SendEmailSMTP.sendFromGMail(new String[]{appt.get().mDoctorUsername, appt.get().mPatientUsername}, "Appointment Cancelled", emailBody);
            }
        }

        result.put("isAppointmentDeleted", isAppointmentDeleted);
        return ResponseEntity.ok(result);
    }
}