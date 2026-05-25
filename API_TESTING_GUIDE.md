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
  "name": "iPhone Charger",
  "slug": "iphone-charger",
  "sku": "IPHONECHARGER",
  "shortDescription": "Fast charging adapter",
  "description": "20W Apple charger",
  "regularPrice": 2999,
  "salePrice": 2499,
  "taxPercentage": 18,
  "stockQuantity": 50,
  "featuredImage": "https://cdn.example.com/charger.jpg",
  "status": "ACTIVE",
  "categoryId": 1,
  "brandId": 1,

  "variants": [
    {
      "variantName": "Color",
      "variantValue": "White",
      "sku": "IPHONECHARGER-WHITE",
      "price": 2499,
      "stock": 20
    }
  ],

  "images": [
    {
      "imagePath": "https://cdn.example.com/charger-front.jpg",
      "sortOrder": 1,
      "altText": "Front View"
    }
  ]
}
```

### Expected Response

```json
{
  "success": true,
  "message": "Product created successfully",
  "data": {
    "id": 8,
    "name": "iPhone Charger",
    "slug": "iphone-charger",
    "sku": "IPHONECHARGER",
    "status": "ACTIVE"
  },
  "timestamp": "2026-05-26T12:30:00"
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

## 💻 MacBook Air M3

### Request

```http
POST /api/products
Authorization: Bearer <ADMIN_JWT_TOKEN>
Content-Type: application/json
```

### Request Body

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

### Expected Response

```json
{
  "success": true,
  "message": "Product created successfully",
  "data": {
    "id": 6,
    "name": "MacBook Air M3",
    "slug": "macbook-air-m3",
    "sku": "MACBOOKAIRM3",
    "status": "ACTIVE"
  },
  "timestamp": "2026-05-26T12:30:00"
}
```

---

## 🎮 Gaming Laptop

### Request

```http
POST /api/products
Authorization: Bearer <ADMIN_JWT_TOKEN>
Content-Type: application/json
```

### Request Body

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

### Expected Response

```json
{
  "success": true,
  "message": "Product created successfully",
  "data": {
    "id": 7,
    "name": "Gaming Laptop",
    "slug": "gaming-laptop",
    "sku": "GAMINGLAPTOP01",
    "status": "ACTIVE"
  },
  "timestamp": "2026-05-26T12:35:00"
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