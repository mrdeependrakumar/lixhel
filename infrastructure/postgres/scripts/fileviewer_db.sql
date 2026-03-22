-- ============================
-- Create Database
-- ============================
CREATE DATABASE fileviewer_db;

-- ============================
-- Files Table
-- ============================
CREATE TABLE files (
    id UUID PRIMARY KEY,
    file_name VARCHAR(255),
    file_type VARCHAR(100),
    file_path VARCHAR(500),
    status VARCHAR(50),
    created_at TIMESTAMP
);

-- ============================
-- File Metadata
-- ============================
CREATE TABLE file_metadata (
    id UUID PRIMARY KEY,
    file_id UUID,
    key VARCHAR(255),
    value VARCHAR(255)
);

-- ============================
-- File Access Log
-- ============================
CREATE TABLE file_access_log (
    id UUID PRIMARY KEY,
    file_id UUID,
    accessed_by VARCHAR(255),
    timestamp TIMESTAMP
);

-- ============================
-- Outbox Events
-- ============================
CREATE TABLE outbox_events (
    id UUID PRIMARY KEY,
    event_id UUID,
    event_type VARCHAR(255),
    aggregate_id UUID,
    payload JSONB,
    status VARCHAR(50),
    created_at TIMESTAMP,
    processed_at TIMESTAMP
);

-- ============================
-- Indexes
-- ============================
CREATE INDEX idx_file_metadata_file ON file_metadata(file_id);
CREATE INDEX idx_outbox_status ON outbox_events(status);