# Event Catalog

## Overview

This document defines all events used in the system.
The architecture follows an **event-driven approach using Kafka**, where services communicate asynchronously via well-defined events.

Each event follows a versioned naming convention:

```
<domain>.<action>.v<version>
```

Example:

```
enrollment.created.v1
```

---

# Enrollment Events

## 1. enrollment.created.v1

**Description:**
Triggered when a new user enrollment is successfully created.

## 2. enrollment.updated.v1

**Description:**
Triggered when enrollment details are updated.

## 3. enrollment.failed.v1

**Description:**
Triggered when enrollment processing fails.


---


# FileViewer Events

## 4. file.upload.requested.v1

**Description:**
Triggered when a file upload is initiated.


## 5. file.upload.completed.v1

**Description:**
Triggered when file upload is successfully completed.


## 6. file.processing.failed.v1

**Description:**
Triggered when file processing fails.

# Notification Events

## 7. notification.requested.v1

**Description:**
Triggered when a notification needs to be sent.


## 8. notification.sent.v1

**Description:**
Triggered when a notification is successfully sent.

## 9. notification.failed.v1

**Description:**
Triggered when notification delivery fails.

---

# System / Failure Events

## 10. system.failure.v1

**Description:**
Generic system-level failure event.


## 11. system.dlq.v1

**Description:**
Triggered when a message is moved to Dead Letter Queue (DLQ).

----

# Event Producers & Consumers

| Event                     | Producer            | Consumers                      |
| ------------------------- | ------------------- | ------------------------------ |
| enrollment.created.v1     | Enrollment Domain   | FileViewer, Notification, Saga |
| file.upload.requested.v1  | FileViewer Domain   | Saga                           |
| file.upload.completed.v1  | FileViewer Domain   | Notification, Saga             |
| notification.requested.v1 | Saga                | Notification Domain            |
| notification.sent.v1      | Notification Domain | Saga                           |
| system.failure.v1         | Any Service         | Failure Services               |



