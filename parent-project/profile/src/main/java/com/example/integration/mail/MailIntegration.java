package com.example.integration.mail;

import com.example.integration.mail.dto.MailRequest;

/**
 * Интерфейс для интеграции с сервисом отправки писем.
 */
public interface MailIntegration {

    /**
     * Отправляет email.
     *
     * @param mailRequest запрос на отправку письма
     */
    void sendMail(MailRequest mailRequest);

}
