package com.bluit.tourgatronomico.email;

public interface MailSender {
    void send(String to, String subject, String html);
}
