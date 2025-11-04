# 🚀 Explore With Me — Microservice System (Spring Boot + Spring Cloud)

**Project Description**  
Explore With Me is a distributed event management platform built with a microservice architecture.  
It allows users to create, browse, and participate in public events, leave comments, and view activity statistics.  
The system is designed for scalability, modularity, and easy maintenance through independent Spring Boot services managed by Spring Cloud.

---

## 🧩 Architecture Overview

The project follows a **microservice architecture**, where each service handles a single business domain and communicates with others via REST APIs.  
All services are connected through **Eureka Discovery**, **Config Server**, and **API Gateway** for unified access and configuration.

---

## ⚙️ System Components

| Layer | Component | Description |
|--------|------------|-------------|
| **Infra** | **Gateway Server** | Central entry point for external requests; routes API calls to appropriate services. |
|  | **Config Server** | Centralized configuration storage for all services. |
|  | **Discovery Server (Eureka)** | Registers and discovers all active microservices dynamically. |
| **Core** | **User Service** | Manages users and their data. |
|  | **Event Service** | Handles event creation, editing, and publication. |
|  | **Request Service** | Manages user participation requests in events. |
|  | **Comment Service** | Handles comments associated with events. |
|  | **Interaction API** | Common DTOs and interfaces for internal service interaction. |
| **Stats** | **Stats Server** | Collects and provides statistics for API requests and user interactions. |

---

## 🔄 Service Communication

Each service, on startup:
1. Registers with the **Discovery Server**.
2. Loads configuration from the **Config Server**.
3. Exposes REST APIs through the **Gateway Server**.
4. Interacts with other services using HTTP (JSON).

This design ensures **fault tolerance**, **independent scaling**, and **seamless updates**.

---

## 🌐 API Overview

### 👤 User Service
| Method | Endpoint | Description |
|--------|-----------|-------------|
| `GET` | `/admin/users` | Retrieve all users |
| `GET` | `/admin/users/{id}` | Find user by ID |
| `GET` | `/admin/{id}` | Verify if user exists |

### 🎉 Event Service
| Method | Endpoint | Description |
|--------|-----------|-------------|
| `GET` | `/events/{id}` | Retrieve event details |
| `POST` | `/users/{userId}/events` | Create a new event |
| `PATCH` | `/admin/events/{id}` | Update event info |
| `GET` | `/admin/events/{id}` | Check event existence |

### 📨 Request Service
| Method | Endpoint | Description |
|--------|-----------|-------------|
| `GET` | `/users/{userId}/requests` | Retrieve all user requests |
| `POST` | `/users/{userId}/requests` | Create a new request |
| `PATCH` | `/users/{userId}/requests/{requestId}/cancel` | Cancel a request |
| `GET` | `/requests/event/{eventId}` | Get all requests for an event |
| `PATCH` | `/requests/status/{id}` | Change request status |

### 💬 Comment Service
| Method | Endpoint | Description |
|--------|-----------|-------------|
| `GET` | `/comments/{eventId}` | Get comments for an event |
| `POST` | `/users/{userId}/comments` | Add a new comment |
| `PATCH` | `/users/{userId}/comments/{commentId}` | Edit a comment |
| `DELETE` | `/users/{userId}/comments/{commentId}` | Delete a comment |

### 📊 Stats Service
| Method | Endpoint | Description |
|--------|-----------|-------------|
| `POST` | `/hit` | Record a hit for statistics |
| `GET` | `/stats` | Retrieve aggregated statistics |

---

## 🐳 Running with Docker Compose

### 📦 Requirements
- **Docker**
- **Docker Compose**
- Access to the repository source code

### 🚀 Commands

#### 1. Build and start all services
```bash
docker-compose up --build -d
