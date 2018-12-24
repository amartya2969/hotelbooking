/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hotel.booking;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 *
 * @author AmartyaVats
 */
public class Mailer extends Thread {
    
    String to;
    String subject;
    String body;
    
    Mailer(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.start();
    }
    
    @Override
    public void run() {
        sendMail();
    }

    public void sendMail() {
        try {
            Email email = new SimpleEmail();
            email.setHostName("smtp.googlemail.com");
            email.setSmtpPort(465);
            email.setAuthentication("xyz@example.com", "password");
            email.setSSLOnConnect(true);
            email.setFrom("xyz@example.com");
            email.setSubject(subject);
            email.setMsg(body);
            email.addTo(to);
            email.send();
        } catch (EmailException ex) {
            Logger.getLogger(Mailer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
