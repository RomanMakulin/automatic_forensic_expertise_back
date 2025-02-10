package com.example.auth.integrations.mail;

import com.example.auth.api.dto.MailRequest;

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
