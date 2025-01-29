package com.mindhub.email_service.services;

import com.mindhub.email_service.models.EmailDetails;

public interface EmailService {
    void sendEmail(EmailDetails emailDetails);
    void sendEmailWithAttachment(String to,
                                 String subject,
                                 String body,
                                 String pathToAttachment);
    //void sendTemplateEmail(String to,String subject,String... templateArgs);
}
