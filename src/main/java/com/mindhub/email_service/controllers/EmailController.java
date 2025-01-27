package com.mindhub.email_service.controllers;

import com.mindhub.email_service.services.impl.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/api/email")
public class EmailController {
    @Autowired
    private EmailServiceImpl emailService;

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestParam String to,
                                            @RequestParam String subject,
                                            @RequestParam String body) {
        emailService.sendEmail(to, subject, body);
        return ResponseEntity.ok("Email sent successfully!");
    }

    @PostMapping("/send-with-attachment")
    public ResponseEntity<String> sendEmailWithAttachment(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body,
            @RequestParam("attachment") MultipartFile attachment) {

        try {
            File tempFile = File.createTempFile("attachment-", attachment.getOriginalFilename());
            attachment.transferTo(tempFile);

            emailService.sendEmailWithAttachment(to, subject, body, tempFile.getAbsolutePath());

            tempFile.delete();

            return ResponseEntity.ok("Correo con adjunto enviado exitosamente!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error enviando correo: " + e.getMessage());
        }
    }

    @PostMapping("/send-template")
    public String sendTemplateEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String name,
            @RequestParam String message) {

        emailService.sendTemplateEmail(to, subject, name, message);
        return "Correo con template enviado exitosamente!";
    }
}
