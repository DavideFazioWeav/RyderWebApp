package co.develhope.myApp.notification.services;

import co.develhope.myApp.users.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailNotificationService {

    @Autowired
    private JavaMailSender emailSender;


    public void sendActivationMail(User user) {
        SimpleMailMessage sms = new SimpleMailMessage();
        sms.setTo(user.getEmail());
        sms.setFrom("dfcompositions@gmail.com");
        sms.setReplyTo("dfcompositions@gmail.com");
        sms.setSubject("Iscrizione piattaforma");
        sms.setText("Il codice di attivazione è: "+ user.getActivationCode());
        /*sms.setText("Clicca qui per completare l'attivazione: https://www.mioSito.it/signup/activation/" );*/

        emailSender.send(sms);

    }

    public void sendPasswordResetMail(User user) {
        SimpleMailMessage sms = new SimpleMailMessage();
        sms.setTo(user.getEmail());
        sms.setFrom("dfcompositions@gmail.com");
        sms.setReplyTo("dfcompositions@gmail.com");
        sms.setSubject("Reset Password");
        sms.setText("Il codice di attivazione è: "+ user.getPasswordResetCode());
        emailSender.send(sms);
    }
}
