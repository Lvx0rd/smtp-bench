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
     * Crea una sessione SMTP utilizzando le proprietà configurate
     * Utilizza l'autenticazione con username e password
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
     * Invia un'email creando ogni volta una nuova sessione
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
     * Invia un'email utilizzando una sessiobne e un transport esistenti
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
        return (end - start) / 1_000_000; // tempo in ms
    }

    /**
     * Invia un'email e misura il tempo impiegato per l'invio
     */
    public long sendMailAndMeasure(String subject, String body) throws MessagingException {
        long start = System.nanoTime();
        sendMail(subject, body);
        long end = System.nanoTime();
        return (end - start) / 1_000_000; // tempo in millisecondi
    }
}