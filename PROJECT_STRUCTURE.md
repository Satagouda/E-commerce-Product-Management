# Spring Boot E-Commerce Project Structure

## 📁 Complete Project File Organization

```
spring-boot-ecommerce/
│
├── 📄 pom.xml                                    # Maven dependencies and project configuration
├── 📄 docker-compose.yml                        # Docker services (PostgreSQL, Redis, Meilisearch)
├── 📄 Dockerfile                                # Spring Boot app container configuration
├── 📄 README.md                                 # Comprehensive project documentation
├── 📄 .gitignore                                # Git ignore rules
│
├── src/main/java/com/ecommerce/
│   │
│   ├── 📄 ECommerceApplication.java             # Main Spring Boot entry point
│   │
│   ├── controller/
│   │   └── 📄 ProductController.java            # REST API endpoints for products
│   │
│   ├── service/
│   │   ├── 📄 ProductService.java               # Product business logic
│   │   │   - getAllProducts()
│   │   │   - getProductById()
│   │   │   - searchProducts()
│   │   │   - createProduct()
│   │   │   - updateProduct()
│   │   │   - deleteProduct()
│   │   │
│   │   └── 📄 CategoryService.java              # Category & Brand business logic
│   │       - getAllCategories()
│   │       - createBrand()
│   │
│   ├── repository/
│   │   └── 📄 RepositoriesBundle.java           # Spring Data JPA repositories
│   │       - ProductRepository
│   │       - CategoryRepository
│   │       - BrandRepository
│   │       - UserRepository
│   │       - ProductVariantRepository
│   │       - ProductImageRepository
│   │       - ProductAttributeRepository
│   │       - RoleRepository
│   │
│   ├── entity/
│   │   ├── 📄 BaseEntity.java                   # Base class with audit fields
│   │   │   - id, createdAt, updatedAt, version, active
│   │   │
│   │   ├── 📄 Product.java                      # Product entity
│   │   │   - name, slug, price, stockQuantity
│   │   │   - @ManyToOne Category, Brand
│   │   │   - @OneToMany ProductVariant, ProductImage
│   │   │
│   │   ├── 📄 Category.java                     # Category entity
│   │   ├── 📄 Brand.java                        # Brand entity
│   │   │
│   │   ├── 📄 ProductVariant.java               # Product variants (size, color)
│   │   ├── 📄 ProductImage.java                 # Product images & variants
│   │   ├── 📄 ProductAttribute.java             # Product specifications
│   │   │
│   │   ├── 📄 User.java                         # User entity
│   │   │   - implements UserDetails
│   │   │   - @ManyToMany Role
│   │   │
│   │   └── 📄 Role.java                         # Role entity (ADMIN, USER, SELLER)
│   │
│   ├── dto/
│   │   ├── 📄 ProductDTO.java                   # Product API DTO
│   │   ├── 📄 DTOBundle.java                    # Other DTOs
│   │   │   - ProductVariantDTO
│   │   │   - ProductImageDTO
│   │   │   - ProductAttributeDTO
│   │   │   - CategoryDTO
│   │   │   - BrandDTO
│   │   │
│   │   └── auth/
│   │       └── 📄 AuthDTOs.java
│   │           - LoginRequest
│   │           - RegisterRequest
│   │           - AuthResponse
│   │           - UserDTO
│   │           - RefreshTokenRequest
│   │           - ChangePasswordRequest
│   │
│   ├── security/
│   │   ├── 📄 JwtTokenProvider.java             # JWT token generation & validation
│   │   │   - generateAccessToken()
│   │   │   - generateRefreshToken()
│   │   │   - validateToken()
│   │   │   - getUsernameFromToken()
│   │   │
│   │   └── 📄 JwtAuthenticationFilter.java      # JWT filter & entry point
│   │       - OncePerRequestFilter
│   │       - JwtAuthenticationEntryPoint
│   │
│   ├── config/
│   │   ├── 📄 SecurityConfig.java               # Spring Security configuration
│   │   │   - SecurityFilterChain
│   │   │   - PasswordEncoder (BCrypt)
│   │   │   - CORS configuration
│   │   │
│   │   ├── 📄 CacheConfig.java                  # Redis cache configuration
│   │   │   - @EnableCaching
│   │   │   - RedisTemplate
│   │   │
│   │   ├── 📄 AuditingConfig.java               # JPA auditing configuration
│   │   │   - @EnableJpaAuditing
│   │   │
│   │   └── 📄 OpenApiConfig.java                # Swagger/OpenAPI configuration
│   │       - Swagger UI settings
│   │       - API documentation
│   │
│   └── exception/
│       ├── 📄 AppExceptions.java                # Custom exception classes
│       │   - ResourceNotFoundException
│       │   - BadRequestException
│       │   - UnauthorizedException
│       │   - ForbiddenException
│       │   - ConflictException
│       │
│       └── 📄 GlobalExceptionHandler.java       # Global exception handling
│           - @RestControllerAdvice
│           - Error response formatting
│
├── src/main/resources/
│   │
│   ├── 📄 application.yml                       # Spring Boot configuration
│   │   - Database: PostgreSQL
│   │   - Cache: Redis
│   │   - Search: Meilisearch
│   │   - Security: JWT settings
│   │   - Logging: Debug levels
│   │
│   └── db/migration/
│       └── 📄 V1__initial_schema.sql            # Flyway database migration
│           - Create all tables
│           - Create indexes
│           - Foreign key relationships
│
└── src/test/
    └── ... (JUnit 5 + Mockito tests)
```

## 🔍 Key Files by Purpose

### Configuration Files
- `pom.xml` - Maven dependencies (Spring Boot, JPA, Security, JWT, Redis, etc.)
- `application.yml` - Application configuration (DB, cache, JWT secrets)
- `docker-compose.yml` - Local development environment (PostgreSQL, Redis, Meilisearch)

### Entity Layer
- `BaseEntity.java` - Common fields (id, timestamps, version for optimistic locking)
- `Product.java` - Core product entity with @ManyToOne Category/Brand
- `ProductVariant.java` - Size/color variants with independent stock
- `ProductImage.java` - Product images with Cloudinary integration
- `User.java` - User with Spring Security UserDetails
- `Category.java`, `Brand.java` - Master data entities

### Repository Layer
- `ProductRepository.java` - Spring Data JPA with custom JPQL queries
- Supported patterns: `findBySlug()`, `findByCategoryIdAndStatus()`
- Pagination support for large datasets

### Service Layer
- `ProductService.java` - Business logic with @Transactional & @Cacheable
- `CategoryService.java` - Category/Brand operations
- Handles data validation, caching, cache invalidation
- Coordinates with repositories and external services

### DTO Layer
- `ProductDTO.java` - Data transfer object with validation
- `CategoryDTO.java`, `BrandDTO.java` - Category/brand DTOs
- `AuthDTOs.java` - Authentication request/response objects
- Input validation using Jakarta Validation annotations

### Controller Layer
- `ProductController.java` - REST endpoints with OpenAPI documentation
- GET endpoints (read-only, cacheable)
- POST/PUT/DELETE endpoints (@PreAuthorize for ADMIN role)
- Request/response mapping with DTOs

### Security Layer
- `JwtTokenProvider.java` - Token generation, validation, claims extraction
- `JwtAuthenticationFilter.java` - Intercepts requests, validates tokens
- `SecurityConfig.java` - Spring Security configuration with JWT integration

### Configuration Classes
- `SecurityConfig.java` - Authentication & authorization setup
- `CacheConfig.java` - Redis cache configuration
- `AuditingConfig.java` - Automatic timestamp management
- `OpenApiConfig.java` - Swagger UI configuration

### Exception Handling
- `AppExceptions.java` - Custom exception classes for API errors
- `GlobalExceptionHandler.java` - @RestControllerAdvice for consistent error responses

### Database
- `V1__initial_schema.sql` - Flyway migration with complete schema
- Tables: users, products, categories, brands, variants, images, attributes
- Indexes on commonly queried columns (slug, status, category_id)
- Foreign key constraints with CASCADE delete

## 🔗 Data Flow Example: Creating a Product

```
1. Client sends POST /api/products with ProductDTO
   │
2. ProductController.createProduct(productDTO)
   │
3. JWT filter validates token and sets authentication
   │
4. @PreAuthorize("hasRole('ADMIN')") checks role
   │
5. @Valid annotation validates productDTO
   │
6. ProductService.createProduct(productDTO) @Transactional
   │
7. Validation: Check if SKU already exists
   │
8. Calculate selling price based on discount
   │
9. ProductRepository.save(product) → PostgreSQL
   │
10. @CacheEvict invalidates old cache
    │
11. (Optional) ApplicationEvent triggers Meilisearch indexing
    │
12. Response: 201 Created + ProductDTO

Time taken: ~50-100ms with caching, ~200-300ms cold start
```

## 📊 Database Relationships

```
┌─────────────┐
│   Users     │
└──────┬──────┘
       │ @ManyToMany
       │
┌──────▼──────┐
│   Roles     │
└─────────────┘

┌─────────────────┐        ┌──────────────┐
│   Categories    │◄───────│   Products   │
└─────────────────┘ @ManyToOne └──────┬──────────┘
                                      │
                              ┌───────┴───────┐
                              │               │
                      ┌───────▼──────┐ ┌─────▼──────────┐
                      │   Variants   │ │    Images      │
                      └──────────────┘ └────────────────┘
                              │
                      ┌───────▼──────┐
                      │ Variant Imgs │
                      └──────────────┘

┌──────────────┐
│    Brands    │◄─────────┐
└──────────────┘ @ManyToOne
                           │
                      ┌────▼─────┐
                      │ Products  │
                      └───────────┘

┌────────────────┐
│   Attributes   │─────┐
└────────────────┘ @ManyToOne
                       │
                  ┌────▼─────┐
                  │ Products  │
                  └───────────┘
```

## 🎯 API Features

- **Pagination**: Size 20, sortable by any field
- **Caching**: 24-hour TTL in Redis
- **Search**: Full-text search via Meilisearch
- **Security**: JWT + Role-based access control
- **Validation**: Input validation with error messages
- **Documentation**: Swagger UI at /api/swagger-ui.html

## 🚀 Next Steps to Enhance

1. **Add Order Management** - Order, OrderItem, Payment entities
2. **Add Reviews & Ratings** - Review, Rating entities
3. **Add Wishlist** - Wishlist, WishlistItem entities
4. **Add Cart** - Cart, CartItem for user sessions
5. **Add Admin Dashboard** - Analytics, reporting endpoints
6. **Email Integration** - Send confirmation emails
7. **Payment Gateway** - Stripe, PayPal integration
8. **Inventory Alerts** - Stock notifications
9. **Advanced Search Filters** - Price range, color, size filters
10. **Analytics** - Track user behavior, popular products

---

**Project created with ❤️ following Spring Boot best practices**
