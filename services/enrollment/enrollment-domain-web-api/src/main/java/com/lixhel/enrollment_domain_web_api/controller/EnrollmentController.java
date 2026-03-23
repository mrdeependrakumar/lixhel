package com.lixhel.enrollment_domain_web_api.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lixhel.enrollment_domain_web_api.entity.Enrollment;
import com.lixhel.enrollment_domain_web_api.service.EnrollmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService service;

    @PostMapping
    public Enrollment create(@RequestParam UUID userId) {
        return service.createEnrollment(userId);
    }
}