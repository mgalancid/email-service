package com.mindhub.email_service.services;

public interface EmailService {
    public void sendEmail(String to, String subject, String body);
}
