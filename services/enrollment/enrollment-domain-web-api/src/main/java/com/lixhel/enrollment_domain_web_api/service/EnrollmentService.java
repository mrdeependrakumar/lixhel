package com.lixhel.enrollment_domain_web_api.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.lixhel.enrollment_domain_web_api.entity.Enrollment;
import com.lixhel.enrollment_domain_web_api.event.EnrollmentEventPublisher;
import com.lixhel.enrollment_domain_web_api.repo.EnrollmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository repository;
    private final EnrollmentEventPublisher publisher;
    public Enrollment createEnrollment(UUID userId) {

        Enrollment enrollment = new Enrollment();
        enrollment.setId(UUID.randomUUID());
        enrollment.setUserId(userId);
        enrollment.setStatus("CREATED");
        enrollment.setCreatedAt(LocalDateTime.now());
        enrollment.setUpdatedAt(LocalDateTime.now());
        Enrollment saved = repository.save(enrollment);
     // Publish event
        
        publisher.publishEnrollmentCreated(saved.getId().toString());

        return saved;
    }
}
