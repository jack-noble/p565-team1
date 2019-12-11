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

    public ResponseEntity<?> getTimeSlots(HttpServletRequest request) {
        String username = request.getParameter("username");
        Map<String, Object> result = new HashMap<>();
        List<DBObject> finalList = new ArrayList<>();

        DoctorDetails doctorDetails = new DoctorDetails(username, "");
        Optional<DoctorDetails> doctorQueriedFromDB = doctorRepository.findById(Integer.toString(username.hashCode()));
        List<DBObject> timeSlots = doctorDetails.getTimeSlots();
        if(doctorQueriedFromDB.isPresent()) { timeSlots = doctorQueriedFromDB.get().mTimeSlots; }

        for (int i = 0; i < timeSlots.size(); i++) {
            BasicDBObject ts = new BasicDBObject((Document) timeSlots.get(i));
            if((boolean) ts.get("isAvailable")) {
                finalList.add(ts);
            }
        }

        result.put("TimeSlots", finalList);
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> createAppointments(HttpServletRequest request) throws JsonProcessingException {

        String username = request.getParameter("username");
        String hospital = request.getParameter("hospital");
        String doctorUsername = request.getParameter("doctorUsername");
        String location = request.getParameter("location");
        String time = request.getParameter("time");
        int timeSlotId = Integer.parseInt(request.getParameter("timeSlotId"));

        Map<String, Object> result = new HashMap<>();
        List<DBObject> timeSlots = null;
        Date date = null;
        boolean isAppointmentCreated = false;
        DateFormat inFormat = new SimpleDateFormat( "MMM dd, yyyy");

        try { date = inFormat.parse(request.getParameter("date")); }
        catch ( ParseException e ) { e.printStackTrace(); }

        date.setHours(Integer.parseInt(time.split(":")[0]));

        AppointmentsDetails appointmentsDetails = new AppointmentsDetails(username, doctorUsername, hospital, location, date);
        appointmentsRepository.save(appointmentsDetails);
        isAppointmentCreated = true;

        Optional<DoctorDetails> doctorQueriedFromDB = doctorRepository.findById(Integer.toString(doctorUsername.hashCode()));
        DoctorDetails doctorDetails = new DoctorDetails(doctorUsername, "");

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
<<<<<<< Updated upstream
=======
            for(int i = 0; i < appointmentsList.size(); i++) {
                AppointmentsDetails appointment = appointmentsList.get(i);
                if(now.compareTo(appointment.mDate) > 0) {
                    appointment.setStatus(false);
                    appointmentsRepository.save(appointment);
                    appointmentsList.remove(appointment);
                }
            }

            billingService.getPatientsUnpaidBills(results, appointmentsList, username);
        }

        if(userType.equals(DOCTOR)) {
            appointmentsList = appointmentsRepository.findAllDoctorAppointments(username);
            for(int i = 0; i < appointmentsList.size(); i++) {
                AppointmentsDetails appointment = appointmentsList.get(i);
                if(now.compareTo(appointment.mDate) > 0) {
                    appointment.setStatus(false);
                    appointmentsRepository.save(appointment);
                    appointmentsList.remove(appointment);
                }
            }
        }

        for(AppointmentsDetails appointment : appointmentsList) {
            // Needs to be handled seperately and can't be merged with the above for loops.
            // This is because, if a list size is updated, we get ConcurrentModifiedException.
            // And only in this type of for loop, can we update an object in the list and it stays that way
            handleErroneousData(appointment);
        }

        results.put("CurrentAppointments", appointmentsList);
        List<AppointmentsDetails> pastAppointments = getPastAppointmentsAsList(request, userType);

        for(AppointmentsDetails appointment: appointmentsList) {
            appointment.setDisplayTime(get12HrTime(Integer.valueOf(appointment.getDisplayTime())));
        }
        for(AppointmentsDetails appointment: pastAppointments) {
            appointment.setDisplayTime(get12HrTime(Integer.valueOf(appointment.getDisplayTime())));
        }
        if (pastAppointments.isEmpty()) {
            results.put("PastAppointments", new ArrayList<AppointmentsDetails>());
        } else {
            results.put("PastAppointments", pastAppointments);
        }

        return ResponseEntity.ok(results);
    }

    private void handleErroneousData(AppointmentsDetails appointment) {
        boolean isChanged = false;
        if(appointment.getDisplayTime().contains("M")) {
            //If it contains AM or PM in the 'about to be displayed time'
            appointment.setDisplayTime(String.valueOf(getTimeSlotToStoreInDB(appointment.getDisplayTime(), " ")));
            isChanged = true;
        }

        if(Integer.parseInt(appointment.getDisplayTime()) > 7) {
            appointment.setDisplayTime(String.valueOf(Integer.parseInt(appointment.getDisplayTime()) - 9));
            isChanged = true;
        }

        if(appointment.getDisplayDate().contains("/")) {
            appointment.formatDisplayDate(appointment.getDate());
            isChanged = true;
        }

        if(isChanged) {
            appointmentsRepository.save(appointment);
        }
    }

    public ResponseEntity<?> getPastAppointments(HttpServletRequest request, String userType) {
        Map<String, Object> result = new HashMap();

        result.put("Appointments", getPastAppointmentsAsList(request, userType));
        return ResponseEntity.ok(result);
    }

    private List<AppointmentsDetails> getPastAppointmentsAsList(HttpServletRequest request, String userType) {
        String username = getUsername(request);

        List<AppointmentsDetails> result = new ArrayList();

        if(userType.equals(PATIENT)) {
            List<AppointmentsDetails> appointmentsList = appointmentsRepository.findAllPatientAppointments(username);
>>>>>>> Stashed changes
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

        if(userType.equals(PATIENT)) {
            appointmentsRepository.deleteById(id);
            isAppointmentDeleted = true;
        }

        result.put("isAppointmentDeleted", isAppointmentDeleted);
        return ResponseEntity.ok(result);
    }
}


