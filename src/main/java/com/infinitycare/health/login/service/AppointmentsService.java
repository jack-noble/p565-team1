package com.infinitycare.health.login.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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

        // 0:9, 1:10, 2:11, 3:12, 4:1, 5:2, 6:3, 7:4.
        List<Integer> availableTimeSlots = new ArrayList<>();
        Collections.addAll(availableTimeSlots, 0, 1, 2, 3, 4, 5, 6, 7);

        String date = request.getParameter("date"); // Date format: mm/dd/yyyy
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

    public ResponseEntity<?> createAppointments(HttpServletRequest request) throws JsonProcessingException {

        String username = request.getParameter("username");
        String doctorusername = request.getParameter("doctorusername");
        String time = request.getParameter("time");
        String doctorpassword = request.getParameter("doctorpassword");
        String datestring = request.getParameter("date");

        Date date = null;
        boolean isAppointmentCreated = false;
        AppointmentsDetails appointmentsDetails = null;
        Map<String, Object> result = new HashMap<>();

        DateFormat inFormat = new SimpleDateFormat( "mm/dd/yyyy");

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

            DoctorDetails doctorDetails = new DoctorDetails(doctorusername, doctorpassword);
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

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(appointmentsDetails);

        SendEmailSMTP.sendFromGMail(new String[]{username, doctorusername}, "Appointment Confirmed", json);

        result.put("isAppointmentCreated", isAppointmentCreated);
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> getAppointments(HttpServletRequest request, String userType) {
        // doing a sanity check when sending appointments to the user.

        String username = request.getParameter(USERNAME);
        Map<String, Object> result = new HashMap<>();
        List<AppointmentsDetails> appointmentsList = null;
        Date now = new Date();

        if(userType.equals(PATIENT)) {
            appointmentsList = appointmentsRepository.findAllPatientAppointments(username);
            for (AppointmentsDetails appointmentsDetails : appointmentsList) {
                if (now.compareTo(appointmentsDetails.getDate()) > 0) {
                    appointmentsDetails.setStatus(false);
                    appointmentsRepository.save(appointmentsDetails);
                    appointmentsList.remove(appointmentsDetails);
                }
            }
        }

        if(userType.equals(DOCTOR)) {
            appointmentsList = appointmentsRepository.findAllDoctorAppointments(username);
            System.out.println(appointmentsList);
            for (AppointmentsDetails appointmentsDetails : appointmentsList) {
                if (now.compareTo(appointmentsDetails.mDate) > 0) {
                    appointmentsDetails.setStatus(false);
                    appointmentsRepository.save(appointmentsDetails);
                }
            }
        }

        result.put("Appointments", appointmentsList);
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> getPastAppointments(HttpServletRequest request, String userType) {
        String username = request.getParameter(USERNAME);
        Map<String, Object> result = new HashMap<>();
        List<AppointmentsDetails> appointmentsList = null;

        if(userType.equals(PATIENT)) {
            appointmentsList = appointmentsRepository.findAllPatientAppointments(username);
            for (AppointmentsDetails appointmentsDetails : appointmentsList) {
                if (appointmentsDetails.getStatus()) {
                    appointmentsList.remove(appointmentsDetails);
                }
            }
        }

        if(userType.equals(DOCTOR)) {
            appointmentsList = appointmentsRepository.findAllDoctorAppointments(username);
            for (AppointmentsDetails appointmentsDetails : appointmentsList) {
                if (appointmentsDetails.getStatus()) {
                    appointmentsList.remove(appointmentsDetails);
                }
            }
        }

        result.put("Appointments", appointmentsList);
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> cancelAppointments(HttpServletRequest request, String userType) throws JsonProcessingException {
        String id = request.getParameter("id");
        boolean isAppointmentDeleted = false;
        Map<String, Object> result = new HashMap<>();

        if(userType.equals(PATIENT) || userType.equals(DOCTOR)) {
            Optional<AppointmentsDetails> appt = appointmentsRepository.findById(id);
            if(appt.isPresent()) {
                appointmentsRepository.deleteById(id);
                isAppointmentDeleted = true;
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String json = ow.writeValueAsString(appt);
                SendEmailSMTP.sendFromGMail(new String[]{appt.get().mDoctorUsername, appt.get().mPatientUsername}, "Appointment Cancelled", json);
            }
        }

        result.put("isAppointmentDeleted", isAppointmentDeleted);
        return ResponseEntity.ok(result);
    }
}