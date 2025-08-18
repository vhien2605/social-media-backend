package com.hien.back_end_app.services;


import com.hien.back_end_app.exceptions.AppException;
import com.hien.back_end_app.utils.enums.ErrorCode;
import com.hien.back_end_app.utils.enums.MailTemplateType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender emailSender;

    @Async
    public void sendMessage(String from, String to, MailTemplateType type, MultipartFile multipartFile) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(type.getSubject());
            helper.setText(type.getText());
            if (multipartFile != null) {
                helper.addAttachment(Objects.requireNonNull(multipartFile.getOriginalFilename()), new ByteArrayResource(multipartFile.getBytes()));
            }
            emailSender.send(message);
        } catch (MessagingException | IOException e) {
            log.error("error", e);
            throw new AppException(ErrorCode.MAIL_SERVER_ERROR);
        }
    }
}
