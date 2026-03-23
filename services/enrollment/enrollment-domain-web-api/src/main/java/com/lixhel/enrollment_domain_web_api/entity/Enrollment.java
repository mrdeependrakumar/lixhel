package com.lixhel.enrollment_domain_web_api.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "enrollments")
@Data
public class Enrollment {

    @Id
    private UUID id;

    private UUID userId;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
