package com.example.mailnotification.controller;

import com.example.mailnotification.dto.SendRequest;
import com.example.mailnotification.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для отправки писем для неавторизованных пользователей
 */
@RestController
@RequestMapping("/api/public-notification")
public class PublicController {

    /**
     * Сервис для отправки писем
     */
    private final EmailService emailService;

    public PublicController(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Отправка письма
     *
     * @param sendRequest объект для отправки письма
     * @return статус отправки
     */
    @PostMapping("/send")
    public ResponseEntity<Void> resetPassword(@RequestBody SendRequest sendRequest) {
        emailService.sendEmail(sendRequest.getTo(), sendRequest.getSubject(), sendRequest.getBody());
        return ResponseEntity.ok().build();
    }

}
