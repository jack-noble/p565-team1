package com.infinitycare.health.login;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class SendEmailSMTP {

    public static void sendFromGMail(String[] recipients, String subject, String body) {
        String USER_NAME = "infinitycare247";  // GMail user name (just the part before "@gmail.com")
        String PASSWORD = "mycare@360"; // GMail password

        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", USER_NAME);
        props.put("mail.smtp.password", PASSWORD);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(USER_NAME));
            InternetAddress[] toAddress = new InternetAddress[recipients.length];

            // To get the array of addresses
            for( int i = 0; i < recipients.length; i++ ) {
                toAddress[i] = new InternetAddress(recipients[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            //message.setText(body);
            message.setContent("<a href=\"http://localhost:8080\">Please verify your account signup with InfinityCare!</a>", "text/html");
            Transport transport = session.getTransport("smtp");
            transport.connect(host, USER_NAME, PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}