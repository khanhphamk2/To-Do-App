package org.khanhpham.todo.service;

public interface EmailService {
    void sendEmail(String to, String subject, String content);
}
