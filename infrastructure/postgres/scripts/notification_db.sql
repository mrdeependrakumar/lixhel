-- ============================
-- Create Database
-- ============================
CREATE DATABASE notification_db;

-- ============================
-- Notifications Table
-- ============================
CREATE TABLE notifications (
    id UUID PRIMARY KEY,
    user_id UUID,
    type VARCHAR(50),
    message TEXT,
    status VARCHAR(50),
    created_at TIMESTAMP
);

-- ============================
-- Notification Templates
-- ============================
CREATE TABLE notification_templates (
    id UUID PRIMARY KEY,
    type VARCHAR(50),
    template_body TEXT,
    created_at TIMESTAMP
);

-- ============================
-- Notification Logs
-- ============================
CREATE TABLE notification_logs (
    id UUID PRIMARY KEY,
    notification_id UUID,
    status VARCHAR(50),
    attempt_count INT,
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
CREATE INDEX idx_notification_user ON notifications(user_id);
CREATE INDEX idx_outbox_status ON outbox_events(status);