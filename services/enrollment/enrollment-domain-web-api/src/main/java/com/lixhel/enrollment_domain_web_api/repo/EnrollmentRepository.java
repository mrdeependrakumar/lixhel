package com.lixhel.enrollment_domain_web_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lixhel.enrollment_domain_web_api.entity.Enrollment;

import java.util.UUID;

public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {
}