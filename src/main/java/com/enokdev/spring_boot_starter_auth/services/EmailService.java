package com.enokdev.spring_boot_starter_auth.services;

import com.enokdev.spring_boot_starter_auth.api.IEmailService;
import com.enokdev.spring_boot_starter_auth.exeption.EmailSendException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class EmailService implements IEmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${app.support.email}")
    private String supportEmail;

    @Override
    public void sendConfirmationEmail(String to, String token) {
        try {
            String confirmationUrl = baseUrl + "/api/auth/confirm-email?token=" + token;

            Context context = new Context();
            context.setVariable("confirmationUrl", confirmationUrl);

            String content = templateEngine.process("confirmation-email", context);
            sendHtmlEmail(to, "Confirm your email", content);

            log.info("Confirmation email sent to: {} with URL: {}", to, confirmationUrl);
        } catch (Exception e) {
            log.error("Failed to send confirmation email to: {}", to, e);
            throw new EmailSendException("Failed to send confirmation email", e);
        }
    }

    @Override
    public void sendPasswordResetEmail(String to, String token) {
        try {
            String resetUrl = baseUrl + "/api/auth/reset-password?token=" + token;

            Context context = new Context();
            context.setVariable("resetUrl", resetUrl);
            context.setVariable("token", token);
            context.setVariable("expirationHours", 24); // Durée de validité du token

            String content = templateEngine.process("reset-password-email", context);
            sendHtmlEmail(to, "Reset your password", content);

            log.info("Password reset email sent to: {} with URL: {}", to, resetUrl);
        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}", to, e);
            throw new EmailSendException("Failed to send password reset email", e);
        }
    }

    @Override
    public void sendAccountLockedEmail(String to) {
        try {
            Context context = new Context();
            context.setVariable("supportEmail", supportEmail);
            context.setVariable("lockTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            context.setVariable("ipAddress", getCurrentIpAddress());
            context.setVariable("unlockTime", LocalDateTime.now().plusHours(24)
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            String content = templateEngine.process("account-locked-email", context);
            sendHtmlEmail(to, "Account Security Alert - Account Locked", content);

            log.info("Account locked email sent to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send account locked email to: {}", to, e);
            throw new EmailSendException("Failed to send account locked email", e);
        }
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.debug("Email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to: {} - Error: {}", to, e.getMessage());
            throw new EmailSendException("Failed to send email", e);
        }
    }

    private String getCurrentIpAddress() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest().getRemoteAddr();
        } catch (Exception e) {
            return "Unknown";
        }
    }
}