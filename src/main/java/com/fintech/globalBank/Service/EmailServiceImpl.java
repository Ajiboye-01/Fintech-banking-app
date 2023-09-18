package com.fintech.globalBank.Service;

import com.fintech.globalBank.dto.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{

    //Javamail sender is in-built
    @Autowired
    private JavaMailSender javaMailSender;

    private String senderEmail;


    /**
     * Using the in-built Simple mail Message, it is used to set the standard body of a mail
     * This gets the Email detail of both the sender and the recipient using the inbuilt
     * method named EMAIL-DETAILS.
     * Then set the body of the email that has the normal standard details of sending an email
     */

    //sender email is the Value
    @Value("${spring.mail.username")
    @Override
    public void sendEmail(EmailDetails emailDetails) {
        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(senderEmail);
            mailMessage.setTo(emailDetails.getRecipient());
            mailMessage.setText(emailDetails.getMessageBody());
            mailMessage.setSubject(emailDetails.getSubject());

            javaMailSender.send(mailMessage);
            System.out.println("Mail sent Successfully");
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
