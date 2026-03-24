package com.lixhel.enrollment_domain_web_api.outbox;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lixhel.enrollment_domain_web_api.entity.OutboxEvent;
import com.lixhel.enrollment_domain_web_api.repo.OutboxRepository;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedRate = 5000) // runs every 5 seconds
    public void publishEvents() {

        List<OutboxEvent> events = outboxRepository.findByStatus("PENDING");

        for (OutboxEvent event : events) {
            try {
                kafkaTemplate.send(
                        "enrollment.created.v1",
                        event.getAggregateId().toString(),
                        event.getPayload()
                );

                event.setStatus("SENT");
                event.setProcessedAt(LocalDateTime.now());

                outboxRepository.save(event);

            } catch (Exception e) {
                System.out.println("Kafka publish failed, will retry...");
            }
        }
    }
}
