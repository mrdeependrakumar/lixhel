package com.lixhel.fileviewer_domain_web_api.consumer;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.lixhel.fileviewer_domain_web_api.entity.FileMetadata;
import com.lixhel.fileviewer_domain_web_api.events.EnrollmentCreatedEvent;
import com.lixhel.fileviewer_domain_web_api.repo.FileMetadataRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RequiredArgsConstructor
@Component
public class EnrollmentEventConsumer {

	private final FileMetadataRepository repository;
	private final ObjectMapper objectMapper;
    @KafkaListener(
        topics = "enrollment.created.v1",
        groupId = "fileviewer-group"
    )
    
   

    
    public void consume(String message) {

        log.info("Received event: {}", message);
        
        EnrollmentCreatedEvent event = objectMapper.readValue(message, EnrollmentCreatedEvent.class);
        
        UUID enrollmentId = UUID.fromString(event.getEnrollmentId());

        FileMetadata file = new FileMetadata();
        file.setId(UUID.randomUUID());
        file.setEnrollmentId(enrollmentId);
        file.setFileUrl("PENDING"); // placeholder
        file.setStatus("RECEIVED");
        file.setCreatedAt(LocalDateTime.now());

        repository.save(file);

        log.info("File metadata saved");
    }
}