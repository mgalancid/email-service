package com.mindhub.email_service.services.impl;

import com.mindhub.email_service.dtos.*;
import com.mindhub.email_service.models.EmailDetails;
import com.mindhub.email_service.services.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import com.mindhub.email_service.utils.PdfGenerator;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailServiceImpl implements EmailService {
    private static final Log log = LogFactory.getLog(EmailServiceImpl.class);
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    /// Rabbit Listener

    @RabbitListener(queues = "registerUser")
    public void processUserRegistration(NewUserEntityDTO newUserDTO) {
        try {
            if (newUserDTO.getEmail() == null || newUserDTO.getEmail().isEmpty()) {
                log.error("No recipient email address found. Email cannot be sent.");
                return;
            }

            EmailDetails emailDetails = new EmailDetails(
                    "noreply@example.com",
                    newUserDTO.getEmail(),
                    "Welcome!",
                    "Hello " + newUserDTO.getUsername() + ", welcome to our platform!"
            );

            sendEmail(emailDetails);
            log.info("Welcome email sent successfully to: " + emailDetails.getTo());
        } catch (Exception e) {
            log.error("Error processing user registration message: ", e);
        }
    }

    @RabbitListener(queues = "createOrder")
    public void receiveOrder(NewOrderEntityDTO newOrderEntityDTO) {
        if (newOrderEntityDTO == null) {
            throw new RuntimeException("Received null order data from RabbitMQ");
        }

        String userEmail = newOrderEntityDTO.getUserEmail();

        StringBuilder body = getStringBuilder(newOrderEntityDTO, userEmail);

        PdfGenerator pdfGenerator = new PdfGenerator();

        String pdfPath = null;
        try {
            pdfPath = pdfGenerator.createPdf(newOrderEntityDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }

        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setFrom(sender);
        emailDetails.setTo(userEmail);
        emailDetails.setSubject("Order Confirmation");
        emailDetails.setBody(body.toString());

        sendEmailWithAttachment(emailDetails.getTo(), emailDetails.getSubject(), emailDetails.getBody(), pdfPath);
    }

    private static StringBuilder getStringBuilder(NewOrderEntityDTO newOrderEntityDTO, String userEmail) {
        StringBuilder body = new StringBuilder("Dear " + userEmail + ",\n\n" +
                "Your order has been created successfully. Your order status is: " +
                newOrderEntityDTO.getStatus() + ".\n\n");

        body.append("Order Details:\n");

        for (NewOrderItemEntityDTO item : newOrderEntityDTO.getProducts()) {
            body.append("- Product ID: ")
                    .append(item.getProductId())
                    .append(", Quantity: ")
                    .append(item.getQuantity()).append("\n");
        }

        body.append("\nThank you for shopping with us!");
        return body;
    }

    @RabbitListener(queues = "confirmOrder")
    public void processOrderConfirmation(OrderEntityDTO orderEntityDTO) {
        try {
            if (orderEntityDTO == null) {
                log.error("Received null order confirmation data from RabbitMQ");
                return;
            }

            StringBuilder body = new StringBuilder("Dear Customer,\n\n" +
                    "Your order with ID " + orderEntityDTO.getId() + " has been confirmed.\n" +
                    "The current status of your order is: " + orderEntityDTO.getStatus() + ".\n\n");

            body.append("Order Details:\n");

            for (OrderItemDTO item : orderEntityDTO.getProducts()) {
                body.append("- Product ID: ")
                        .append(item.getProductId())
                        .append(", Quantity: ")
                        .append(item.getQuantity()).append("\n");
            }

            body.append("\nThank you for shopping with us!");

            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setFrom("noreply@example.com");
            emailDetails.setSubject("Order Confirmation");
            emailDetails.setBody(body.toString());

            sendEmail(emailDetails);
            log.info("Confirmation email sent successfully to: " + emailDetails.getTo());
        } catch (Exception e) {
            log.error("Error processing order confirmation message: ", e);
        }
    }

    /// Service implementation

    @Override
    public void sendEmail(EmailDetails emailDetails) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(emailDetails.getTo());
        message.setSubject(emailDetails.getSubject());
        message.setText(emailDetails.getBody());

        mailSender.send(message);
        log.info("Welcome email sent successfully to: " + emailDetails.getTo());
    }

    @Override
    public void sendEmailWithAttachment(String to, String subject, String body, String pathToAttachment) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("noreply@example.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);

            FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
            helper.addAttachment(file.getFilename(), file);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error enviando correo con adjunto", e);
        }
    }
}
