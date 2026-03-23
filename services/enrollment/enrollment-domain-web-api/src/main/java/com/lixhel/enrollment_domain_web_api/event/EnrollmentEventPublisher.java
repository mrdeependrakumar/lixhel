package com.lixhel.enrollment_domain_web_api.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnrollmentEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "enrollment.created.v1";

    public void publishEnrollmentCreated(String payload) {
        kafkaTemplate.send(TOPIC, payload);
    }
}
