package com.infinitycare.health.login.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.infinitycare.health.database.AppointmentsRepository;
import com.infinitycare.health.database.DoctorRepository;
import com.infinitycare.health.database.PatientRepository;
import com.infinitycare.health.login.SendEmailSMTP;
import com.infinitycare.health.login.model.AppointmentsDetails;
import com.infinitycare.health.login.model.CookieDetails;
import com.infinitycare.health.login.model.DoctorDetails;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.BsonDocument;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AppointmentsService extends CookieDetails {

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
        // String username = request.getParameter("username");
        Map<String, Object> result = new HashMap<>();
        List<DBObject> finalList = new ArrayList<>();

        DoctorDetails doctorDetails = new DoctorDetails(doctorusername, "");
        Optional<DoctorDetails> doctorQueriedFromDB = doctorRepository.findById(Integer.toString(doctorusername.hashCode()));
        List<DBObject> timeSlots = doctorDetails.getTimeSlots();
        if(doctorQueriedFromDB.isPresent()) { timeSlots = doctorQueriedFromDB.get().mTimeSlots; }

        for (int i = 0; i < timeSlots.size(); i++) {
            BasicDBObject ts = new BasicDBObject((Document) timeSlots.get(i));
            if((boolean) ts.get("isAvailable")) {
                ts.remove("isAvailable");
                finalList.add(ts);
            }
        }

        result.put("TimeSlots", finalList);
        return ResponseEntity.ok(result);
    }

    // TODO Figure out the Hospital & Location in the Doctor Profile
    public ResponseEntity<?> createAppointments(HttpServletRequest request) throws JsonProcessingException {

        String username = request.getParameter("username");
        // String hospital = request.getParameter("hospital");
        String doctorUsername = request.getParameter("doctorUsername");
        // String location = request.getParameter("location");
        String time = request.getParameter("time");
        int timeSlotId = Integer.parseInt(request.getParameter("timeSlotId"));

        Map<String, Object> result = new HashMap<>();
        List<DBObject> timeSlots = null;
        Date date = null;
        boolean isAppointmentCreated = false;
        DateFormat inFormat = new SimpleDateFormat( "MMM dd, yyyy");

        Optional<DoctorDetails> doctorQueriedFromDB = doctorRepository.findById(Integer.toString(doctorUsername.hashCode()));
        DoctorDetails doctorDetails = new DoctorDetails(doctorUsername, "");

        try { date = inFormat.parse(request.getParameter("date")); }
        catch ( ParseException e ) { e.printStackTrace(); }

        date.setHours(Integer.parseInt(time.split(":")[0]));

        AppointmentsDetails appointmentsDetails = new AppointmentsDetails(username, doctorUsername, date);
        appointmentsRepository.save(appointmentsDetails);
        isAppointmentCreated = true;

        if(doctorQueriedFromDB.isPresent()) {
            timeSlots = doctorQueriedFromDB.get().mTimeSlots;
            BasicDBObject ts = new BasicDBObject((Document) timeSlots.get(timeSlotId));
            ts.replace("isAvailable", false);
            timeSlots.set(timeSlotId, ts);
            doctorDetails.setTimeSlots(timeSlots);
            doctorRepository.save(doctorDetails);
        }

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(appointmentsDetails);

        SendEmailSMTP.sendFromGMail(new String[]{username}, "Appointment Confirmed", json);
        SendEmailSMTP.sendFromGMail(new String[]{username}, "Appointment Confirmed", json);

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
                }
            }
        }

        if(userType.equals(DOCTOR)) {
            appointmentsList = appointmentsRepository.findAllDoctorAppointments(username);
            for (AppointmentsDetails appointmentsDetails : appointmentsList) {
                if (now.compareTo(appointmentsDetails.getDate()) > 0) {
                    appointmentsDetails.setStatus(false);
                    appointmentsRepository.save(appointmentsDetails);
                }
            }
        }

        result.put("Appointments", appointmentsList);
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> cancelAppointments(HttpServletRequest request, String userType) {
        String id = request.getParameter("id");
        boolean isAppointmentDeleted = false;
        Map<String, Object> result = new HashMap<>();

        if(userType.equals(PATIENT) || userType.equals(DOCTOR)) {
            appointmentsRepository.deleteById(id);
            isAppointmentDeleted = true;
        }

        result.put("isAppointmentDeleted", isAppointmentDeleted);
        return ResponseEntity.ok(result);
    }
}


