package com.lixhel.enrollment_domain_web_api.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.lixhel.enrollment_domain_web_api.entity.Enrollment;
import com.lixhel.enrollment_domain_web_api.entity.OutboxEvent;
import com.lixhel.enrollment_domain_web_api.event.EnrollmentEventPublisher;
import com.lixhel.enrollment_domain_web_api.repo.EnrollmentRepository;
import com.lixhel.enrollment_domain_web_api.repo.OutboxRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository repository;
//    private final EnrollmentEventPublisher publisher;
    private final OutboxRepository outboxRepository;
    @Transactional
    public Enrollment createEnrollment(UUID userId) {

        // Save Enrollment
        Enrollment enrollment = new Enrollment();
        enrollment.setId(UUID.randomUUID());
        enrollment.setUserId(userId);
        enrollment.setStatus("CREATED");
        enrollment.setCreatedAt(LocalDateTime.now());
        enrollment.setUpdatedAt(LocalDateTime.now());

        Enrollment saved = repository.save(enrollment);

        // Save Outbox Event
        OutboxEvent event = new OutboxEvent();
        event.setId(UUID.randomUUID());
        event.setAggregateType("Enrollment");
        event.setAggregateId(saved.getId());
        event.setEventType("ENROLLMENT_CREATED");
        event.setPayload("{\"enrollmentId\":\"" + saved.getId() + "\"}");
        event.setStatus("PENDING");
        event.setCreatedAt(LocalDateTime.now());

        outboxRepository.save(event);

        return saved;
    }
}
