package com.example.neet.Service;

import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	
	
	@Autowired
    private JavaMailSender mailSender;

    // ================= PLAIN TEXT EMAIL =================
	@Async
    public void sendMail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);

        } catch (Exception e) {
            System.out.println("Plain email failed: " + e.getMessage());
        }
    }

    // ================= HTML EMAIL =================
	@Async
    public void sendHtmlMail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // ✅ HTML

            mailSender.send(message);

        } catch (Exception e) {
            System.out.println("HTML email failed: " + e.getMessage());
        }
    }
}