package com.ecommerce.controller;



import com.ecommerce.dto.ProductDTO;
import com.ecommerce.dto.SearchResponseDTO;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
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
            description = "Create a new product",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<ProductDTO> createProduct(
            @Valid @RequestBody ProductDTO productDTO
    ) {

        log.info("POST /api/products - Creating product: {}", productDTO.getName());

        ProductDTO createdProduct =
                productService.createProduct(productDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdProduct);
    }

    /**
     * Get all products
     */
    @GetMapping
    @Operation(summary = "Get all products")
    public ResponseEntity<Page<ProductDTO>> getAllProducts(

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

        return ResponseEntity.ok(
                productService.getAllProducts(
                        page,
                        size,
                        sortBy,
                        sortDir
                )
        );
    }

    /**
     * Get product by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<ProductDTO> getProductById(
            @PathVariable Long id
    ) {

        log.info("GET /api/products/{}", id);

        return ResponseEntity.ok(
                productService.getProductById(id)
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
    public ResponseEntity<ProductDTO> updateProduct(

            @PathVariable Long id,

            @Valid @RequestBody ProductDTO productDTO
    ) {

        log.info("PUT /api/products/{}", id);

        return ResponseEntity.ok(
                productService.updateProduct(id, productDTO)
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
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id
    ) {

        log.info("DELETE /api/products/{}", id);

        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Get products by category
     */
    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get products by category")
    public ResponseEntity<Page<ProductDTO>> getProductsByCategory(

            @PathVariable Long categoryId,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size
    ) {

        return ResponseEntity.ok(
                productService.getProductsByCategory(
                        categoryId,
                        page,
                        size
                )
        );
    }

    /**
     * Get products by brand
     */
    @GetMapping("/brand/{brandId}")
    @Operation(summary = "Get products by brand")
    public ResponseEntity<Page<ProductDTO>> getProductsByBrand(

            @PathVariable Long brandId,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size
    ) {

        return ResponseEntity.ok(
                productService.getProductsByBrand(
                        brandId,
                        page,
                        size
                )
        );
    }

//    /**
//     * Search products
//     */
//    @GetMapping("/search")
//    @Operation(summary = "Search products")
//    public ResponseEntity<Page<ProductDTO>> searchProducts(
//
//            @RequestParam String keyword,
//
//            @RequestParam(defaultValue = "0")
//            int page,
//
//            @RequestParam(defaultValue = "10")
//            int size
//    ) {
//
//        return ResponseEntity.ok(
//                productService.searchProducts(
//                        keyword,
//                        page,
//                        size
//                )
//        );
//    }

    @GetMapping("/search")
    @Operation(summary = "Smart search products")
    public ResponseEntity<SearchResponseDTO> searchProducts(
            @RequestParam String keyword
    ) {

        return ResponseEntity.ok(
                searchService.searchProducts(keyword)
        );
    }
}