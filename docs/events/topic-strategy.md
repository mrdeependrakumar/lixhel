# 📊 Kafka Topic Strategy

---

## 1. 🧾 Naming Convention

All Kafka topics follow a consistent naming pattern:

```
<domain>.<action>.<version>
```

### Examples:

* enrollment.created.v1
* file.upload.requested.v1
* file.upload.completed.v1
* notification.sent.v1
* system.failure.v1
* system.dlq.v1

### Rules:

* Use lowercase
* Use dot (`.`) as separator
* Include version (`v1`)
* Follow domain-driven naming

---

## 2. 📂 Topic Classification

Topics are categorized by domain:

### Enrollment Topics

* enrollment.created.v1
* enrollment.updated.v1
* enrollment.failed.v1

### FileViewer Topics

* file.upload.requested.v1
* file.upload.completed.v1
* file.processing.failed.v1

### Notification Topics

* notification.requested.v1
* notification.sent.v1
* notification.failed.v1

### System Topics

* system.failure.v1
* system.dlq.v1

---

## 3. ⚙️ Partition Strategy

Each topic will have:

* **Partitions:** 3
* **Replication Factor:** 1 (for development)

### Production Recommendation:

* **Partitions:** 6–12 (based on load)
* **Replication Factor:** 3

---

## 4. 🔑 Message Key Strategy

Message keys ensure ordering.

### Key Selection:

| Topic          | Key            |
| -------------- | -------------- |
| enrollment.*   | enrollmentId   |
| file.*         | fileId         |
| notification.* | notificationId |

### Example:

```
Key: "enrollment-123"
```

### Why Key Matters:

* Ensures event order per entity
* Controls partition placement
* Improves scalability

---

## 5. 🗂️ Retention Policy

### Default Retention:

* 7 days

### Custom Retention:

| Topic          | Retention |
| -------------- | --------- |
| system.dlq.v1  | 14 days   |
| notification.* | 3 days    |
| audit topics   | 30 days   |

---

## 6. 👥 Consumer Groups

Each service has its own consumer group.

### Groups:

* enrollment-group
* fileviewer-group
* notification-group
* saga-group
* failure-handler-group

### Rules:

* One group per service
* Multiple instances share same group
* Different services use different groups

---

## 7. 🔁 Event Delivery Guarantees

We follow:

**At-Least-Once Delivery**

### Handling Strategy:

* Consumers must be idempotent
* Use `eventId` to prevent duplicates

---

## 8. ☠️ Dead Letter Queue (DLQ)

Failed messages are redirected to:

```
system.dlq.v1
```

### Scenarios:

* Processing failure
* Schema mismatch
* Timeout

### DLQ Flow:

```
Consumer fails
   ↓
Retry (3 times)
   ↓
Send to DLQ
```

---

## 9. 🔄 Retry Strategy

### Configuration:

* **Retry Count:** 3
* **Backoff:** Exponential

### Example:

* 1st retry → 1 sec
* 2nd retry → 5 sec
* 3rd retry → 10 sec

---

## 10. 📦 Schema Management

Schemas are managed using:

* Schema Registry

### Format:

* Avro (preferred)
* JSON (initial phase)

### Schema Versioning:

* Backward compatible changes only

---

## 11. 🏷️ Topic Ownership

Each topic has a clear owner.

| Topic          | Owner                |
| -------------- | -------------------- |
| enrollment.*   | Enrollment Service   |
| file.*         | FileViewer Service   |
| notification.* | Notification Service |
| system.*       | Platform             |

---

## 12. 🔐 Security (Future Scope)

* SSL encryption
* SASL authentication
* Role-based access

---

## 13. 📈 Monitoring

Monitor topics using:

* Kafka UI
* Prometheus
* Grafana

### Track:

* Consumer lag
* Throughput
* Failures

---
