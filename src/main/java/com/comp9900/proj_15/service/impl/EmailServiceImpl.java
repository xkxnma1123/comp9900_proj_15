package com.comp9900.proj_15.service.impl;

import com.comp9900.proj_15.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * 邮件服务实现
 */
@Service
public class EmailServiceImpl implements EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Override
    public void sendVerificationEmail(String to, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Account Verification Code");
            
            String content = "<div style='font-family: Arial, sans-serif; padding: 20px; max-width: 600px; margin: 0 auto; border: 1px solid #e0e0e0; border-radius: 5px;'>"
                    + "<h2 style='color: #333;'>Account Verification</h2>"
                    + "<p>Hello, </p>"
                    + "<p>Thanks for your signup. please use the following verification code: </p>"
                    + "<div style='background-color: #f5f5f5; padding: 15px; text-align: center; margin: 20px 0;'>"
                    + "<span style='font-size: 24px; font-weight: bold; letter-spacing: 5px; color: #333;'>" + code + "</span>"
                    + "</div>"
                    + "<p>Will expire in 10 mins</p>"
                    + "<p>If it is not you, igonre this email</p>"
                    + "<hr style='border: none; border-top: 1px solid #e0e0e0; margin: 20px 0;'>"
                    + "<p style='font-size: 12px; color: #777;'>It is a auto email, please do not reply.</p>"
                    + "</div>";
            
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("发送邮件失败: " + e.getMessage());
        }
    }
}