package com.lixhel.fileviewer_domain_web_api.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "file_metadata")
@Data
public class FileMetadata {

    @Id
    private UUID id;

    private UUID enrollmentId;
    private String fileUrl;
    private String status;
    private LocalDateTime createdAt;
}