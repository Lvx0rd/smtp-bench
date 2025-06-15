package com.smtpbench.app;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class SmtpMailSender {

    private final ConfigLoader config;

    public SmtpMailSender(ConfigLoader config) {
        this.config = config;
    }

    private Properties createSmtpProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", config.get("smtp.host"));
        props.put("mail.smtp.port", config.get("smtp.port"));
        return props;
    }

    /**
     * Creates an SMTP session using the configured properties.
     * Uses authentication with username and password.
     */
    public Session createSession() {
        return Session.getInstance(createSmtpProperties(), new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        config.get("smtp.username"),
                        config.get("smtp.password"));
            }
        });
    }

    /**
     * Sends an email by creating a new session each time.
     */
    public void sendMail(String subject, String body) throws MessagingException {
        Session session = createSession();
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(config.get("smtp.from")));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(config.get("smtp.to")));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }

    /**
     * Sends an email using an existing session and transport.
     */
    public long sendMailOnTransport(Session session, Transport transport, String subject, String body) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(config.get("smtp.from")));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(config.get("smtp.to")));
        message.setSubject(subject);
        message.setText(body);

        long start = System.nanoTime();
        transport.sendMessage(message, message.getAllRecipients());
        long end = System.nanoTime();
        return (end - start) / 1_000_000; // time in ms
    }

    /**
     * Sends an email and measures the time taken to send it.
     */
    public long sendMailAndMeasure(String subject, String body) throws MessagingException {
        long start = System.nanoTime();
        sendMail(subject, body);
        long end = System.nanoTime();
        return (end - start) / 1_000_000; // time in ms
    }
}