# E-commerce Product Management Backend

A production-style Spring Boot backend for an E-commerce Product Management system featuring:

- JWT Authentication & Role-Based Authorization
- PostgreSQL Database
- Redis Caching
- Flyway Database Migrations
- Dockerized Infrastructure
- Swagger/OpenAPI Documentation
- Meilisearch Smart Search
- Typo Tolerance
- Autocomplete
- Synonym Search
- Product / Category / Brand Management APIs

---

# Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Backend Language |
| Spring Boot 3 | Backend Framework |
| Spring Security | JWT Authentication |
| PostgreSQL | Primary Database |
| Redis | Caching |
| Flyway | Database Migration |
| Meilisearch | Smart Search Engine |
| Docker | Containerization |
| Swagger/OpenAPI | API Documentation |
| Maven | Dependency Management |

---

# Features

## Authentication & Authorization

- User Registration
- User Login
- JWT Token Authentication
- Role-Based Access Control (RBAC)
- Admin-only APIs

---

## Product Management

- Create Product
- Update Product
- Delete Product
- Product Pagination
- Product Filtering
- Product Search
- Product Variants
- Product Images

---

## Smart Search Features

Implemented using Meilisearch:

- Typo Tolerance
- Autocomplete
- Search Suggestions
- Synonym Search
- Fast Product Search

### Example

Searching:

```text
iphon
```

Still returns:

```text
iPhone 15 Pro
```

Searching:

```text
notebook
```

Returns:

```text
Gaming Laptop
MacBook Air M3
```

---

# Architecture

```text
Client
   ↓
Controller Layer
   ↓
Service Layer
   ↓
Repository Layer
   ↓
PostgreSQL

Search Flow:

Client
   ↓
SearchController
   ↓
SearchService
   ↓
Meilisearch
```

---

# Project Structure

```text
src/main/java/com/ecommerce
│
├── config
├── controller
├── dto
├── entity
├── exception
├── repository
├── security
├── service
└── util
```

---

# Docker Services

The project uses Docker Compose for infrastructure.

Services:

- PostgreSQL
- Redis
- Meilisearch
- Spring Boot Application

---

# Setup Instructions

## 1. Clone Repository

```bash
git clone <your-repo-url>
cd ecommerce-backend
```

---

## 2. Start Docker Services

```bash
docker-compose up -d
```

---

## 3. Verify Services

| Service | URL |
|---|---|
| Spring Boot | http://localhost:8080/api |
| Swagger | http://localhost:8080/api/swagger-ui/index.html |
| Meilisearch | http://localhost:7700 |
| PostgreSQL | localhost:5432 |
| Redis | localhost:6379 |

---

## 4. Run Application

```bash
mvn spring-boot:run
```

---

# Environment Configuration

Example:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ecommerce_db
    username: postgres
    password: postgres

meilisearch:
  host: http://localhost:7700
  api-key: test-api-key-change-in-production
```

---

# Smart Search Implementation

The project uses Meilisearch for advanced search capabilities.

## Why Meilisearch?

Meilisearch was selected because it provides:

- Built-in typo tolerance
- Fast autocomplete
- Lightweight infrastructure
- Easy integration
- High performance product search
- Simple developer experience

without requiring heavyweight Elasticsearch infrastructure.

---

# Synonym Configuration

Current synonym configuration is YAML-driven for simplicity and rapid development.

Example:

```yaml
search:
  synonyms:
    laptop: notebook,macbook
    notebook: laptop,macbook
    phone: mobile,smartphone
```

In production-scale systems, synonyms would typically be managed dynamically through database-backed configuration or search analytics pipelines.

---

# Future Improvements

- AI-powered Semantic Search
- Vector Embeddings
- Search Analytics
- Recommendation Engine
- Elasticsearch/OpenSearch Support
- Admin Dashboard
- Cloudinary Image Uploads
- CI/CD Pipeline
- Unit & Integration Testing

---

# Author

Satagouda Patil