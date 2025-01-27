package com.mindhub.email_service.services;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
    void sendEmailWithAttachment(String to,
                                 String subject,
                                 String body,
                                 String pathToAttachment);
    void sendTemplateEmail(String to,
                           String subject,
                           String... templateArgs);
}
