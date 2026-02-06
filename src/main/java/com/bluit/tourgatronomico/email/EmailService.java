package com.bluit.tourgatronomico.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final MailSender smtpSender;
    private final MailSender gmailSender;

    @Value("${MAIL_PROVIDER:SMTP}")
    private String mailProvider;

    public EmailService(SmtpMailSender smtpSender,
                        GmailApiMailSender gmailSender) {
        this.smtpSender = smtpSender;
        this.gmailSender = gmailSender;
    }

    public void send(String to, String subject, String html) {
        if ("GMAIL_API".equalsIgnoreCase(mailProvider)) {
            gmailSender.send(to, subject, html);
        } else {
            smtpSender.send(to, subject, html);
        }
    }
}
