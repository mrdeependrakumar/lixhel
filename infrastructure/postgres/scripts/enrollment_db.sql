-- ============================
-- Create Database
-- ============================
CREATE DATABASE enrollment_db;

-- ============================
-- Connect to DB (run separately in psql if needed)
-- \c enrollment_db;
-- ============================

-- ============================
-- Users Table
-- ============================
CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- ============================
-- Enrollments Table
-- ============================
CREATE TABLE enrollments (
    id UUID PRIMARY KEY,
    user_id UUID,
    status VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- ============================
-- Enrollment Documents
-- ============================
CREATE TABLE enrollment_documents (
    id UUID PRIMARY KEY,
    enrollment_id UUID,
    file_id UUID,
    status VARCHAR(50),
    created_at TIMESTAMP
);

-- ============================
-- Outbox Events (CRITICAL)
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
-- Audit Log
-- ============================
CREATE TABLE audit_log (
    id UUID PRIMARY KEY,
    action VARCHAR(255),
    entity VARCHAR(255),
    entity_id UUID,
    timestamp TIMESTAMP
);

-- ============================
-- Indexes
-- ============================
CREATE INDEX idx_enrollment_user ON enrollments(user_id);
CREATE INDEX idx_outbox_status ON outbox_events(status);