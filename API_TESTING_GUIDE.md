# API Testing Guide

Base URL:

```text
http://localhost:8080/api
```

Swagger URL:

```text
http://localhost:8080/api/swagger-ui/index.html
```

---

# AUTH APIs

## Register User

### Endpoint

```http
POST /auth/register
```

### Request Body

```json
{
  "username": "adminuser",
  "email": "admin@example.com",
  "firstName": "Admin",
  "lastName": "User",
  "password": "Admin@123",
  "confirmPassword": "Admin@123"
}
```

---

## Login

### Endpoint

```http
POST /auth/login
```

### Request Body

```json
{
  "emailOrUsername": "adminuser",
  "password": "Admin@123"
}
```

### Expected Response

```json
{
  "accessToken": "jwt-token",
  "refreshToken": "refresh-token",
  "tokenType": "Bearer"
}
```

---

# CATEGORY APIs

## Create Category

### Endpoint

```http
POST /categories
```

### Request Body

```json
{
  "name": "Electronics",
  "slug": "electronics",
  "description": "Electronic devices",
  "imageUrl": "https://cdn.example.com/categories/electronics.jpg",
  "displayOrder": 1,
  "productCount": 0
}
```

---

# BRAND APIs

## Create Brand

### Endpoint

```http
POST /brands
```

### Request Body

```json
{
  "name": "Apple",
  "slug": "apple",
  "description": "Apple electronics",
  "logoUrl": "https://cdn.example.com/apple.png",
  "website": "https://apple.com",
  "productCount": 0
}
```

---

# PRODUCT APIs

## Create Product

### Endpoint

```http
POST /products
```

### Request Body

```json
{
  "name": "iPhone 15 Pro",
  "slug": "iphone-15-pro",
  "sku": "IPHONE15PRO",
  "shortDescription": "Apple flagship smartphone",
  "description": "Latest Apple iPhone",
  "regularPrice": 149999,
  "salePrice": 139999,
  "taxPercentage": 18,
  "stockQuantity": 20,
  "featuredImage": "https://cdn.example.com/iphone15.jpg",
  "status": "ACTIVE",
  "categoryId": 1,
  "brandId": 1
}
```

---

# SEARCH API TESTING

## Typo Tolerance

### Request

```http
GET /products/search?keyword=iphon
```

### Expected Result

Returns:

```text
iPhone 15 Pro
```

---

## Autocomplete

### Request

```http
GET /products/search?keyword=lap
```

### Expected Result

Returns:

```text
Gaming Laptop
MacBook Air M3
```

---

## Synonym Search

### Request

```http
GET /products/search?keyword=notebook
```

### Expected Result

Returns:

```text
Gaming Laptop
MacBook Air M3
```

---

# Additional Product Test Data

## MacBook Air M3

```json
{
  "name": "MacBook Air M3",
  "slug": "macbook-air-m3",
  "sku": "MACBOOKAIRM3",
  "shortDescription": "Apple lightweight laptop",
  "description": "Latest Apple MacBook Air with M3 chip",
  "regularPrice": 129999,
  "salePrice": 124999,
  "taxPercentage": 18,
  "stockQuantity": 15,
  "featuredImage": "https://cdn.example.com/macbook.jpg",
  "status": "ACTIVE",
  "categoryId": 1,
  "brandId": 1
}
```

---

## Gaming Laptop

```json
{
  "name": "Gaming Laptop",
  "slug": "gaming-laptop",
  "sku": "GAMINGLAPTOP01",
  "shortDescription": "High performance gaming laptop",
  "description": "RTX powered gaming notebook",
  "regularPrice": 159999,
  "salePrice": 149999,
  "taxPercentage": 18,
  "stockQuantity": 12,
  "featuredImage": "https://cdn.example.com/gaminglaptop.jpg",
  "status": "ACTIVE",
  "categoryId": 1,
  "brandId": 2
}
```

---

# Sample CURL Commands

## Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{
  "emailOrUsername": "adminuser",
  "password": "Admin@123"
}'
```

---

## Search Products

```bash
curl "http://localhost:8080/api/products/search?keyword=iphon"
```

---

# Notes

- Use JWT token in Swagger Authorize button.
- Prefix token with:

```text
Bearer <token>
```

- Ensure Docker services are running before testing APIs.