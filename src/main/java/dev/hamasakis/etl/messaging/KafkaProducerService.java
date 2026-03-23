package dev.hamasakis.etl.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    
    private final KafkaTemplate<String, String> kafkaTemplate;
    
    private static final String TOPIC_FILE_UPLOADED = "etl.file.uploaded";
    
    public void sendFileUploadedMessage(String fileName) {
        log.info("Publishing file uploaded event to Kafka topic: {}. File URI: {}", TOPIC_FILE_UPLOADED, fileName);

        try {
            kafkaTemplate.send(TOPIC_FILE_UPLOADED, fileName);
            log.info("Event successfully published to Kafka");

        } catch (Exception e) {
            log.error("Failed to publish event to Kafka topic: {}", TOPIC_FILE_UPLOADED, e);
            throw new RuntimeException("Error communicating with Kafka broker", e);
        }
    }
    
}
