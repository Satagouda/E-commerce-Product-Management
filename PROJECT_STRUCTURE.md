# Project Structure

```text
src/main/java/com/ecommerce
│
├── config/
│   ├── 📄 AuditingConfig.java           # JPA auditing configuration
│   ├── 📄 CacheConfig.java              # Redis cache configuration
│   ├── 📄 MeiliConfig.java              # Meilisearch client configuration
│   ├── 📄 OpenApiConfig.java            # Swagger/OpenAPI configuration
│   ├── 📄 SearchInitializer.java        # Initializes Meilisearch settings
│   ├── 📄 SearchProperties.java         # YAML-based synonym configuration
│   └── 📄 SecurityConfig.java           # Spring Security + JWT config
│
├── controller/
│   ├── 📄 AuthController.java           # Authentication APIs
│   ├── 📄 BrandController.java          # Brand management APIs
│   ├── 📄 CategoryController.java       # Category management APIs
│   ├── 📄 ProductController.java        # Product APIs + Smart Search
│   └── 📄 UserController.java           # User management APIs
│
├── dto/
│   │
│   ├── auth/authDTOs/
│   │   ├── 📄 AuthResponse.java
│   │   ├── 📄 ChangePasswordRequest.java
│   │   ├── 📄 LoginRequest.java
│   │   ├── 📄 RefreshTokenRequest.java
│   │   ├── 📄 RegisterRequest.java
│   │   └── 📄 UserDTO.java
│   │
│   ├── DTOBundles/
│   │   ├── 📄 BrandDTO.java
│   │   ├── 📄 CategoryDTO.java
│   │   ├── 📄 ProductImageDTO.java
│   │   └── 📄 ProductVariantDTO.java
│   │
│   ├── 📄 ProductDTO.java
│   ├── 📄 SearchDocument.java
│   └── 📄 SearchResponseDTO.java
│
├── entity/
│   ├── 📄 BaseEntity.java               # Common audit fields
│   ├── 📄 Brand.java
│   ├── 📄 Category.java
│   ├── 📄 Product.java
│   ├── 📄 ProductImage.java
│   ├── 📄 ProductVariant.java
│   ├── 📄 Role.java
│   └── 📄 User.java
│
├── exception/
│   │
│   ├── customException/
│   │   ├── 📄 BadRequestException.java
│   │   ├── 📄 ConflictException.java
│   │   ├── 📄 ForbiddenException.java
│   │   ├── 📄 InternalServerException.java
│   │   ├── 📄 ResourceNotFoundException.java
│   │   └── 📄 UnauthorizedException.java
│   │
│   └── 📄 GlobalExceptionHandler.java
│
├── repository/
│   │
│   ├── Bundle/
│   │   ├── 📄 BrandRepository.java
│   │   ├── 📄 ProductImageRepository.java
│   │   ├── 📄 ProductVariantRepository.java
│   │   ├── 📄 RoleRepository.java
│   │   └── 📄 UserRepository.java
│   │
│   ├── 📄 CategoryRepository.java
│   └── 📄 ProductRepository.java
│
├── security/
│   │
│   ├── Filter/
│   │   ├── 📄 JwtAuthenticationEntryPoint.java
│   │   └── 📄 JwtAuthenticationFilter.java
│   │
│   ├── 📄 CustomUserDetailsService.java
│   └── 📄 JwtTokenProvider.java
│
├── service/
│   ├── 📄 AuthService.java              # Authentication business logic
│   ├── 📄 BrandService.java             # Brand business logic
│   ├── 📄 CategoryService.java          # Category business logic
│   ├── 📄 ProductService.java           # Product business logic
│   ├── 📄 SearchService.java            # Meilisearch integration
│   └── 📄 UserService.java              # User business logic
│
└── 📄 ECommerceApplication.java
```

---

# Smart Search Features

Implemented using Meilisearch:

- Typo Tolerance
- Autocomplete
- Synonym Search
- Fast Product Search
- YAML-based Synonym Configuration

---

# Security Features

- JWT Authentication
- Stateless Authorization
- BCrypt Password Encryption
- Role-Based Access Control (RBAC)
- Admin Protected APIs

---

# Redis Usage

Redis is used for caching frequently accessed APIs such as:

- Categories
- Brands
- Product lookups

to improve performance and reduce database load.

---

# Swagger Documentation

Swagger UI:

```text
http://localhost:8080/api/swagger-ui/index.html
```

---

# Search Architecture

```text
Client
   ↓
ProductController
   ↓
SearchService
   ↓
Meilisearch
```

---

# Authentication Flow

```text
User Login
   ↓
JWT Token Generated
   ↓
Authorization Header
   ↓
JWT Filter Validation
   ↓
Protected APIs Access
```

---

# Planned Improvements

- AI-powered Semantic Search
- Vector Embeddings
- Recommendation Engine
- Search Analytics
- Elasticsearch/OpenSearch Support
- Cloudinary Image Uploads
- CI/CD Pipeline
- Unit & Integration Testing

## 📊 Database Relationships

## User ↔ Role

```text
Many-to-Many
```

A user can have multiple roles and a role can belong to multiple users.

Example:

```text
USER
ADMIN
```

Implemented using:

```text
user_roles
```

join table.

---

## Category ↔ Product

```text
One-to-Many
```

A category can contain multiple products.

Example:

```text
Electronics
 ├── iPhone
 ├── MacBook
 └── Gaming Laptop
```

---

## Brand ↔ Product

```text
One-to-Many
```

A brand can contain multiple products.

Example:

```text
Apple
 ├── iPhone
 ├── MacBook
 └── Apple Watch
```

---

## Product ↔ ProductVariant

```text
One-to-Many
```

A product can have multiple variants.

Example:

```text
iPhone 15
 ├── 128GB
 ├── 256GB
 └── 512GB
```

---

## Product ↔ ProductImage

```text
One-to-Many
```

A product can contain multiple images.

Example:

```text
iPhone 15
 ├── front-view.jpg
 ├── side-view.jpg
 └── back-view.jpg
```

---

# Entity Relationship Overview

```text
User
 └──< user_roles >── Role

Category
 └── Product
        ├── ProductVariant
        └── ProductImage

Brand
 └── Product
```
