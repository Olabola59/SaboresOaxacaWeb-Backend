/*package com.bluit.tourgatronomico.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    // Primero intenta app.mail.from, si no existe usa spring.mail.username
    @Value("${app.mail.from:${spring.mail.username:}}")
    private String from;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String to, String subject, String body) {
        if (from == null || from.trim().isEmpty()) {
            throw new IllegalStateException("Falta configurar app.mail.from o spring.mail.username (MAIL_USERNAME).");
        }

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setFrom(from.trim());
        msg.setSubject(subject);
        msg.setText(body);

        mailSender.send(msg);
    }
}
*/

package com.bluit.tourgatronomico.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from:${spring.mail.username:}}")
    private String from;

    @Value("${app.mail.from-name:Sabor Oaxaca}")
    private String fromName;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String to, String subject, String body) {
        if (from == null || from.trim().isEmpty()) {
            throw new IllegalStateException("Falta configurar app.mail.from o spring.mail.username (MAIL_USERNAME).");
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            //MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");




            helper.setTo(to);
            helper.setFrom(from.trim(), fromName);
            helper.setSubject(subject);

            // Texto plano + HTML (mejor entregabilidad en Gmail)
            String html = "<div style='font-family:Arial,sans-serif;line-height:1.5'>"
                    + body.replace("&", "&amp;")
                          .replace("<", "&lt;")
                          .replace(">", "&gt;")
                          .replace("\n", "<br>")
                    + "</div>";

            helper.setText(body, html);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error enviando correo: " + e.getMessage(), e);
        }
    }
}
