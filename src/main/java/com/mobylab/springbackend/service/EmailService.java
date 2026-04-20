package com.mobylab.springbackend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${notification.email.from}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendNewReviewNotification(String toEmail, String username, String albumTitle, int rating) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("New Review Published - " + albumTitle);
            message.setText(String.format(
                    "Hello!\n\n%s has published a new review for \"%s\" with a rating of %d/10.\n\n" +
                            "Check it out on Album Review!\n\nBest regards,\nAlbum Review Team",
                    username, albumTitle, rating));
            mailSender.send(message);
            logger.info("Notification email sent to {}", toEmail);
        } catch (Exception e) {
            logger.error("Failed to send notification email to {}: {}", toEmail, e.getMessage());
        }
    }
}
