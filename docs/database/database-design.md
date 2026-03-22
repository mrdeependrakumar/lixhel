# Database Design Document

## 1. Overview

This document defines the database architecture for the event-driven microservices platform.

The system follows:

* **Database per Service Pattern**
* **Event-Driven Architecture**
* **Outbox Pattern for reliable messaging**
* **Loose coupling between services**

---

## 2. Database Strategy

Each microservice owns its database to ensure:

* Data isolation
* Independent scalability
* Loose coupling

### Databases:

| Service Domain | Database Name   |
| -------------- | --------------- |
| Enrollment     | enrollment_db   |
| FileViewer     | fileviewer_db   |
| Notification   | notification_db |

---

## 3. Common Design Principles

### 3.1 No Cross-Service Joins

* Services must not join tables from other databases
* Communication happens via Kafka events

### 3.2 Use UUIDs

* All primary keys use UUID
* Ensures uniqueness across services

### 3.3 Timestamps

All tables include:

* `created_at`
* `updated_at`

---

## 4. Enrollment Database (enrollment_db)

### 4.1 Tables

#### users

| Column     | Type      | Description    |
| ---------- | --------- | -------------- |
| id         | UUID (PK) | Unique user ID |
| name       | VARCHAR   | User name      |
| email      | VARCHAR   | User email     |
| created_at | TIMESTAMP | Created time   |
| updated_at | TIMESTAMP | Updated time   |

---

#### enrollments

| Column     | Type      | Description       |
| ---------- | --------- | ----------------- |
| id         | UUID (PK) | Enrollment ID     |
| user_id    | UUID      | Reference to user |
| status     | VARCHAR   | Enrollment status |
| created_at | TIMESTAMP | Created time      |
| updated_at | TIMESTAMP | Updated time      |

---

#### enrollment_documents

| Column        | Type      | Description        |
| ------------- | --------- | ------------------ |
| id            | UUID (PK) | Document ID        |
| enrollment_id | UUID      | Related enrollment |
| file_id       | UUID      | File reference     |
| status        | VARCHAR   | Document status    |
| created_at    | TIMESTAMP | Created time       |

---

#### outbox_events (Critical Table)

| Column       | Type      | Description         |
| ------------ | --------- | ------------------- |
| id           | UUID (PK) | Internal ID         |
| event_id     | UUID      | Unique event ID     |
| event_type   | VARCHAR   | Event type          |
| aggregate_id | UUID      | Entity reference    |
| payload      | JSONB     | Event payload       |
| status       | VARCHAR   | NEW / SENT / FAILED |
| created_at   | TIMESTAMP | Created time        |
| processed_at | TIMESTAMP | Processed time      |

---

#### audit_log (Optional)

| Column    | Type      | Description      |
| --------- | --------- | ---------------- |
| id        | UUID      | Log ID           |
| action    | VARCHAR   | Action performed |
| entity    | VARCHAR   | Entity name      |
| entity_id | UUID      | Entity ID        |
| timestamp | TIMESTAMP | Event time       |

---

### 4.2 Relationships

* users → enrollments (1:N)
* enrollments → enrollment_documents (1:N)

---

## 5. FileViewer Database (fileviewer_db)

### 5.1 Tables

#### files

| Column     | Type      | Description  |
| ---------- | --------- | ------------ |
| id         | UUID (PK) | File ID      |
| file_name  | VARCHAR   | File name    |
| file_type  | VARCHAR   | Type         |
| file_path  | VARCHAR   | Storage path |
| status     | VARCHAR   | File status  |
| created_at | TIMESTAMP | Created time |

---

#### file_metadata

| Column  | Type      | Description    |
| ------- | --------- | -------------- |
| id      | UUID (PK) | Metadata ID    |
| file_id | UUID      | File reference |
| key     | VARCHAR   | Metadata key   |
| value   | VARCHAR   | Metadata value |

---

#### file_access_log

| Column      | Type      | Description    |
| ----------- | --------- | -------------- |
| id          | UUID      | Log ID         |
| file_id     | UUID      | File reference |
| accessed_by | VARCHAR   | User           |
| timestamp   | TIMESTAMP | Access time    |

---

#### outbox_events

(Same structure as Enrollment)

---

### 5.2 Relationships

* files → file_metadata (1:N)

---

## 6. Notification Database (notification_db)

### 6.1 Tables

#### notifications

| Column     | Type      | Description          |
| ---------- | --------- | -------------------- |
| id         | UUID (PK) | Notification ID      |
| user_id    | UUID      | User reference       |
| type       | VARCHAR   | EMAIL / SMS / IN_APP |
| message    | TEXT      | Notification content |
| status     | VARCHAR   | SENT / FAILED        |
| created_at | TIMESTAMP | Created time         |

---

#### notification_templates

| Column        | Type      | Description      |
| ------------- | --------- | ---------------- |
| id            | UUID      | Template ID      |
| type          | VARCHAR   | Template type    |
| template_body | TEXT      | Template content |
| created_at    | TIMESTAMP | Created time     |

---

#### notification_logs

| Column          | Type      | Description            |
| --------------- | --------- | ---------------------- |
| id              | UUID      | Log ID                 |
| notification_id | UUID      | Notification reference |
| status          | VARCHAR   | Status                 |
| attempt_count   | INT       | Retry attempts         |
| timestamp       | TIMESTAMP | Log time               |

---

#### outbox_events

(Same structure as Enrollment)

---

### 6.2 Relationships

* notifications → notification_logs (1:N)

---

## 7. Outbox Pattern

### Purpose

Ensures reliable event publishing to Kafka.

### Flow

1. Business data is saved (e.g., enrollment)
2. Event is inserted into `outbox_events`
3. Background processor reads events
4. Event is published to Kafka
5. Status updated to SENT

---

## 8. Indexing Strategy

### Recommended Indexes

```sql
CREATE INDEX idx_outbox_status ON outbox_events(status);
CREATE INDEX idx_enrollment_user ON enrollments(user_id);
CREATE INDEX idx_file_id ON file_metadata(file_id);
CREATE INDEX idx_notification_user ON notifications(user_id);
```

---

## 9. Data Integrity Rules

* Use UUIDs for all primary keys
* Avoid foreign keys across services
* Maintain referential integrity within service only

---

## 10. Scalability Considerations

* Use partitioning for large tables (future)
* Archive old data
* Optimize queries using indexes

---

## 11. Future Enhancements

* Multi-tenancy support
* Soft deletes
* Audit trail improvements
* Data encryption

---

## 12. Summary

This design ensures:

* High scalability
* Fault tolerance
* Service independence
* Reliable event publishing
* Clean separation of concerns

---