package com.bluit.tourgatronomico.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MailDispatcherService {

    private final SmtpMailSender smtpSender;
    private final GmailApiMailSender gmailSender;

    @Value("${MAIL_PROVIDER:SMTP}")
    private String provider;

    public MailDispatcherService(SmtpMailSender smtpSender, GmailApiMailSender gmailSender) {
        this.smtpSender = smtpSender;
        this.gmailSender = gmailSender;
    }

    public void send(String to, String subject, String html) {
        if ("GMAIL_API".equalsIgnoreCase(provider)) {
            gmailSender.send(to, subject, html);
        } else {
            smtpSender.send(to, subject, html);
        }
    }
}
