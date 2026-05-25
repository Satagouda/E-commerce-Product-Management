package com.ecommerce.controller;

import com.ecommerce.ApiResponse;
import com.ecommerce.ApiResponseUtil;
import com.ecommerce.dto.ProductDTO;
import com.ecommerce.dto.SearchResponseDTO;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product management APIs")
public class ProductController {

    private final ProductService productService;
    private final SearchService searchService;

    /**
     * Create Product
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(
            summary = "Create Product",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(
            @Valid @RequestBody ProductDTO productDTO
    ) {

        log.info("POST /api/products - Creating product: {}", productDTO.getName());

        ProductDTO createdProduct =
                productService.createProduct(productDTO);

        return ApiResponseUtil.created(
                "Product created successfully",
                createdProduct
        );
    }

    /**
     * Get all products
     */
    @GetMapping
    @Operation(summary = "Get all products")
    public ResponseEntity<ApiResponse<Page<ProductDTO>>> getAllProducts(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "id")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String sortDir
    ) {

        log.info("GET /api/products");

        Page<ProductDTO> products =
                productService.getAllProducts(
                        page,
                        size,
                        sortBy,
                        sortDir
                );

        return ApiResponseUtil.success(
                "Products fetched successfully",
                products
        );
    }

    /**
     * Get product by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(
            @PathVariable Long id
    ) {

        log.info("GET /api/products/{}", id);

        ProductDTO product =
                productService.getProductById(id);

        return ApiResponseUtil.success(
                "Product fetched successfully",
                product
        );
    }

    /**
     * Update Product
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(
            summary = "Update Product",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(

            @PathVariable Long id,

            @Valid @RequestBody ProductDTO productDTO
    ) {

        log.info("PUT /api/products/{}", id);

        ProductDTO updatedProduct =
                productService.updateProduct(id, productDTO);

        return ApiResponseUtil.success(
                "Product updated successfully",
                updatedProduct
        );
    }

    /**
     * Delete Product
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(
            summary = "Delete Product",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable Long id
    ) {

        log.info("DELETE /api/products/{}", id);

        productService.deleteProduct(id);

        return ApiResponseUtil.success(
                "Product deleted successfully",
                null
        );
    }

    /**
     * Get products by category
     */
    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get products by category")
    public ResponseEntity<ApiResponse<Page<ProductDTO>>> getProductsByCategory(

            @PathVariable Long categoryId,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size
    ) {

        Page<ProductDTO> products =
                productService.getProductsByCategory(
                        categoryId,
                        page,
                        size
                );

        return ApiResponseUtil.success(
                "Category products fetched successfully",
                products
        );
    }

    /**
     * Get products by brand
     */
    @GetMapping("/brand/{brandId}")
    @Operation(summary = "Get products by brand")
    public ResponseEntity<ApiResponse<Page<ProductDTO>>> getProductsByBrand(

            @PathVariable Long brandId,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size
    ) {

        Page<ProductDTO> products =
                productService.getProductsByBrand(
                        brandId,
                        page,
                        size
                );

        return ApiResponseUtil.success(
                "Brand products fetched successfully",
                products
        );
    }

    /**
     * Smart Search Products
     */
    @GetMapping("/search")
    @Operation(summary = "Smart search products")
    public ResponseEntity<ApiResponse<SearchResponseDTO>> searchProducts(
            @RequestParam String keyword
    ) {

        SearchResponseDTO response =
                searchService.searchProducts(keyword);

        return ApiResponseUtil.success(
                "Search completed successfully",
                response
        );
    }
}