package com.lixhel.fileviewer_domain_web_api.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lixhel.fileviewer_domain_web_api.entity.FileMetadata;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadata, UUID> {
	
}