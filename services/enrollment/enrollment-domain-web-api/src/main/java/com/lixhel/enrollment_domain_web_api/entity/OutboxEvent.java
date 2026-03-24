package com.lixhel.enrollment_domain_web_api.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "outbox_events")
@Data
public class OutboxEvent {

    @Id
    private UUID id;

    private String aggregateType;
    private UUID aggregateId;
    private String eventType;

    @Column(columnDefinition = "jsonb")
    private String payload;

    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
}