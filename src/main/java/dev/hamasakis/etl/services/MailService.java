package dev.hamasakis.etl.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    
    private final JavaMailSender mailSender;
    
    public void sendFileArrivalNotification(String fileName) {
       log.info("Sending email notification for file arrival: {}", fileName); 
       
       try {
           SimpleMailMessage message = new SimpleMailMessage();
           message.setFrom("noreply@hamasakis.dev");
           message.setTo("gabriel@hamasakis.dev");
           message.setSubject("ETL System: New File Ready for processing");
           message.setText(String.format("A new file named '%s' has arrived and is ready for processing in the ETL system.", fileName));
           
           mailSender.send(message);
           
           log.info("Email notification sent successfully for file: {}", fileName);
           
       } catch (Exception e) {
           log.error("Failed to send email notification for file: {}", fileName, e);
       }
    }
    
}
