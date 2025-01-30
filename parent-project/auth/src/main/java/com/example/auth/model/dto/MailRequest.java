package com.example.auth.model.dto;


/**
 * Объект для отправки письма
 */
public class MailRequest {

    /**
     * Адрес получателя
     */
    private String to;

    /**
     * Тема письма
     */
    private String subject;

    /**
     * Содержание письма
     */
    private String body;

    public MailRequest(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "MailRequest{" +
                "to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
