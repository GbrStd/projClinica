package gbrstd.clinica.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendSimpleMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(mailFrom);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            emailSender.send(message);
            log.info("Sending email to: " + to);
        } catch (MailException e) {
            log.error("Error sending email to: " + to);
            log.error(e.getMessage());
        }
    }



}
