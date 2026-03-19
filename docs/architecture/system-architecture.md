# **System Architecture**

## **Overview**

The system is an **Event-Driven Microservices Architecture** built using **Java, Spring Boot, Kafka, and Angular Microfrontends**.
It consists of independent services that communicate asynchronously through events, ensuring **loose coupling, scalability, and resilience**.

---

## **Applications (Core Microservices)**

### **1. Enrollment Service**

* Handles user enrollment and registration workflows
* Validates incoming data (e.g., user details, file uploads)
* Publishes events such as:

  * `UserEnrolledEvent`
  * `EnrollmentProcessedEvent`
* Stores enrollment data in its own database

---

### **2. FileViewer Service**

* Responsible for file tracking and viewing
* Displays end-to-end processing status of files
* Subscribes to Kafka events:

  * `EnrollmentProcessedEvent`
  * `FileProcessedEvent`
* Maintains file metadata and status

---

### **3. Notification Service**

* Handles notification generation (email/SMS/in-app)
* Subscribes to events like:

  * `UserEnrolledEvent`
  * `ErrorOccurredEvent`
* Sends notifications asynchronously
* Ensures retry and failure handling mechanisms

---

## **Platform Services**

### **1. API Gateway**

* Single entry point for all client requests
* Routes requests to appropriate microservices
* Handles:

  * Authentication & Authorization
  * Rate limiting
  * Logging

---

### **2. Config Server**

* Centralized configuration management
* Externalizes properties for all microservices
* Supports dynamic configuration refresh

---

### **3. Service Registry (Eureka/Consul)**

* Enables service discovery
* Each service registers itself and discovers others dynamically

---

### **4. Saga Orchestrator (Optional but Recommended)**

* Manages distributed transactions using Saga pattern
* Ensures consistency across services
* Example:

  * Enrollment → File Processing → Notification

---

### **5. Identity Service**

* Handles authentication and authorization
* Issues JWT tokens
* Can integrate with OAuth2 / OpenID Connect

---

## **Communication**

### **Event-Driven Communication (Kafka)**

* Services communicate asynchronously via **Apache Kafka**
* Producers publish events to topics
* Consumers subscribe to relevant topics

#### **Example Event Flow**

1. Enrollment Service publishes `UserEnrolledEvent`
2. FileViewer Service consumes and updates file status
3. Notification Service consumes and sends notification

#### **Benefits**

* Loose coupling
* High scalability
* Fault tolerance
* Event replay capability

---

## **Databases**

* **Database per service pattern**
* Each microservice owns its database

### Suggested DB:

* **PostgreSQL** (primary relational storage)

### Example:

* Enrollment DB → User & enrollment data
* FileViewer DB → File metadata & status
* Notification DB → Notification logs

---

## **Frontend**

### **Angular Microfrontends**

* Each application has its own frontend module:

  * Enrollment UI
  * FileViewer UI
  * Notification UI

### **Integration**

* Uses Module Federation (Webpack)
* Loaded dynamically into a shell application

### **Benefits**

* Independent deployments
* Team autonomy
* Scalability

---

## **High-Level Flow**

1. User submits enrollment via Angular UI
2. Request goes through API Gateway → Enrollment Service
3. Enrollment Service processes and publishes Kafka event
4. Other services react:

   * FileViewer updates status
   * Notification sends alerts
5. UI reflects updates via APIs or event-driven updates

---

## **Non-Functional Considerations**

### **Scalability**

* Kafka partitions enable horizontal scaling
* Stateless services scale independently

### **Resilience**

* Retry mechanisms in Kafka consumers
* Dead Letter Queues (DLQ)

### **Observability**

* Centralized logging (ELK stack)
* Monitoring (Prometheus + Grafana)
* Distributed tracing (Zipkin)

### **Security**

* JWT-based authentication
* HTTPS for all communication

---

## **Future Enhancements**

* Add **Kafka Streams** for real-time processing
* Implement **CQRS pattern**
* Introduce **Event Sourcing**
* Add **Redis caching layer**

