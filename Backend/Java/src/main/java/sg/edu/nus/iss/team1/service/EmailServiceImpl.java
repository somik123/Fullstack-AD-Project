package sg.edu.nus.iss.team1.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public Boolean SendEmail(String[] receivers, String subject, String text) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setSubject(subject);
            msg.setText(text);
            msg.setTo(receivers);
            javaMailSender.send(msg);
            return true;
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return false;
        }

    }
}
