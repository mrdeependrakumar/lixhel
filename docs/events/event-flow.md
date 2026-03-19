# 🔄 Event Flow Documentation

---

## 📖 Overview

This document describes how events move across services in the system.

It answers:

* ✔ Who produces an event
* ✔ Who consumes it
* ✔ What happens next
* ✔ Full lifecycle of business processes

Think of it as:

👉 *“What happens in the system when a user performs an action?”*

---

## 🧾 1. Basic Enrollment Flow (Conceptual)

### Step-by-step:

1. User creates enrollment
2. Enrollment Domain Service saves data
3. Enrollment Domain publishes → `enrollment.created.v1`
4. FileViewer Service consumes event
5. Notification Service consumes event
6. Saga Orchestrator coordinates next steps

---

# 🔹 Flow 1 — Enrollment → File → Notification (Main Flow)

### Step-by-step:

**Step 1:** User submits enrollment form
↓
**Step 2:** Enrollment BFF receives request
↓
**Step 3:** Enrollment Domain Service saves enrollment in DB
↓
**Step 4:** Outbox event created
↓
**Step 5:** Kafka publishes → `enrollment.created.v1`
↓
**Step 6:** FileViewer Domain Service consumes event
↓
**Step 7:** FileViewer creates file metadata
↓
**Step 8:** FileViewer publishes → `file.upload.requested.v1`
↓
**Step 9:** File processing happens (async/mock)
↓
**Step 10:** FileViewer publishes → `file.upload.completed.v1`
↓
**Step 11:** Notification Service consumes event
↓
**Step 12:** Notification sent (Email/SMS)
↓
**Step 13:** Notification publishes → `notification.sent.v1`
↓
**Step 14:** Saga Orchestrator marks workflow complete

---

# 🔹 Flow 2 — Failure Flow

### Step-by-step:

**Step 1:** Any service fails to process event
↓
**Step 2:** Retry mechanism triggered (3 times)
↓
**Step 3:** If still failing → send to DLQ
↓
Kafka Topic → `system.dlq.v1`
↓
**Step 4:** Failure Service consumes DLQ
↓
**Step 5:** Logs error and retries later or raises alert

---

# 🔹 Flow 3 — Notification Failure Flow

### Step-by-step:

**Step 1:** Notification Service fails to send email/SMS
↓
**Step 2:** Publishes → `notification.failed.v1`
↓
**Step 3:** Notification Failure Service consumes event
↓
**Step 4:** Retry sending notification
↓
**Step 5:** If success → publish `notification.sent.v1`
↓
**Step 6:** If still failing → send to DLQ

---

# 🔹 Flow 4 — Saga Orchestration Flow

### Step-by-step:

**Step 1:** Saga listens to `enrollment.created.v1`
↓
**Step 2:** Saga triggers next step → file upload
↓
**Step 3:** Waits for `file.upload.completed.v1`
↓
**Step 4:** Saga t
