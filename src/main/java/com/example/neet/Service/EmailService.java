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

            message.setFrom("gayathrirajaguru2003@gmail.com"); // 🔥 ADD THIS
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);

            System.out.println("✅ EMAIL SENT SUCCESS");

        } catch (Exception e) {
            System.out.println("❌ Plain email failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ================= HTML EMAIL =================
    @Async
    public void sendHtmlMail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("gayathrirajaguru2003@gmail.com"); // 🔥 ADD THIS
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);

            System.out.println("✅ HTML EMAIL SENT");

        } catch (Exception e) {
            System.out.println("❌ HTML email failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

}