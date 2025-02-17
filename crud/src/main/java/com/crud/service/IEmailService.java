package com.crud.service;


import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.http.ResponseEntity;

import java.util.Properties;

public class IEmailService implements EmailService{

    @Override
    public void sendEmail(String email, String body, int otp) {
        Properties properties = System.getProperties();
        String host = "127.0.0.1";
        String recepent = email;
        String sender = properties.getProperty("username");
        String password = properties.getProperty("password");
        properties.setProperty("mail.smtp.host",host);
        Session session = Session.getDefaultInstance(properties);
        try{
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(recepent)));
            message.setSubject("Forget password mail");
            message.setText("Your Forget password Otp: ");
            Transport.send(message);
            System.out.println("mail Sent Successfully");
        } catch (AddressException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
