package com.example.auth.service.integrations.mail;

import com.example.auth.model.dto.MailRequest;

/**
 * Интерфейс для сервиса отправки писем
 */
public interface MailService {

    /**
     * Отправляет письмо
     *
     * @param mailRequest запрос на отправку письма
     */
    void sendMail(MailRequest mailRequest);

    /**
     * Отправляет письмо (доступ к неавторизованным пользователям)
     *
     * @param mailRequest запрос на отправку письма
     */
    void publicSendMail(MailRequest mailRequest);

}
