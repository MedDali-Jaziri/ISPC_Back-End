package ispc.hermes.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

    @Autowired
    private MailSender mailSender;

    public boolean sendVerificationEmail(String recipientEmail, String type) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);

        // Set the email subject and content based on the type
        if ("verificationLink".equals(type)) {
            message.setSubject("Email verification - HerMeS Application");
            message.setText("Hello Dear,\n\nWelcome to your HerMeS Application.\n\nYour requested for email verification. " +
                    "Kindly use this link to verify your email address:\n\n" +
                    "http://150.145.56.34:3030/api/user/activationLink/" + recipientEmail +
                    "\n\nIf you ignore this email, you will not be able to log in to HerMeS Application.\n\n" +
                    "Thank you very much :)\n\nTechnical service: +39 347 3836207 / +216 53 786 397");
        } else if ("forgetPassword".equals(type)) {
            message.setSubject("Password Reset - HerMeS Application");
            message.setText("Hello Dear,\n\nWelcome to your HerMeS Application.\n\nWe noticed that you forgot your password. " +
                    "No problem! Here is your new password: HerMeS-App.\n\nIf you ignore this email, you will not be able to log in to HerMeS Application.\n\n" +
                    "Thank you very much :)\n\nTechnical service: +39 347 3836207 / +216 53 786 397");
        } else {
            // Unknown type, return false indicating email sending failed
            return false;
        }

        try {
            mailSender.send(message);
            return true; // Email sent successfully
        } catch (MailException ex) {
            ex.printStackTrace();
            return false; // Failed to send email
        }
    }
}
