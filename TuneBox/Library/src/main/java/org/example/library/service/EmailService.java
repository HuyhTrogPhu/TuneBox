package org.example.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    public void sendOrderConfirmationEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendVerificationCode(String email, String verificationCode) {
        String subject = "Mã xác nhận đổi mật khẩu";
        String body = "Mã xác nhận của bạn là: " + verificationCode;
        sendOrderConfirmationEmail(email, subject, body); // Gọi lại phương thức đã có để gửi email
    }

    public void sendPasswordResetLink(String email, String resetPasswordLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Đặt lại mật khẩu của bạn");
        message.setText("Nhấp vào liên kết sau để đổi mật khẩu của bạn: " + resetPasswordLink);
        mailSender.send(message);
    }
}
