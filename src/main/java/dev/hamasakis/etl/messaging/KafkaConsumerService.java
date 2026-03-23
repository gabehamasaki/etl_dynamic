package dev.hamasakis.etl.messaging;

import dev.hamasakis.etl.services.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    
    private final MailService mailService;
    
    @KafkaListener(topics = "etl.file.uploaded", groupId = "etl-processor-group")
    public void consumeFileUploadedEvent(String fileName) {
        log.info("Received event from Kafka. Starting ETL process for file: {}", fileName);
        
        try {
            log.info("Simulating ETL processing for file: {}", fileName);
            mailService.sendFileArrivalNotification(fileName);
            
            log.info("ETL process completed for file: {}", fileName);
        } catch (Exception e) {
            log.error("Error processing file: {}", fileName, e);
        }
        
    }
}
