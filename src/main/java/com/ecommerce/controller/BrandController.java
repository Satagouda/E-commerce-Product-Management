package com.ecommerce.controller;



import com.ecommerce.dto.DTOBundles.BrandDTO;

import com.ecommerce.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    /**
     * Get all brands
     */
    @GetMapping
    @Operation(summary = "Get all brands")
    public ResponseEntity<List<BrandDTO>> getAllBrands() {

        log.info("GET /api/brands - Fetching all brands");

        return ResponseEntity.ok(
                brandService.getAllBrands()
        );
    }

    /**
     * Get brand by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get brand by ID")
    public ResponseEntity<BrandDTO> getBrandById(
            @PathVariable Long id
    ) {

        log.info("GET /api/brands/{} - Fetching brand", id);

        return ResponseEntity.ok(
                brandService.getBrandById(id)
        );
    }

    /**
     * Create brand
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(
            summary = "Create new brand",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Brand created successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<BrandDTO> createBrand(
            @Valid @RequestBody BrandDTO brandDTO
    ) {

        log.info("POST /api/brands - Creating brand: {}", brandDTO.getName());

        BrandDTO createdBrand =
                brandService.createBrand(brandDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdBrand);
    }

    /**
     * Update brand
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(
            summary = "Update brand",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<BrandDTO> updateBrand(
            @PathVariable Long id,
            @Valid @RequestBody BrandDTO brandDTO
    ) {

        log.info("PUT /api/brands/{} - Updating brand", id);

        return ResponseEntity.ok(
                brandService.updateBrand(id, brandDTO)
        );
    }

    /**
     * Delete brand
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(
            summary = "Delete brand",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<Void> deleteBrand(
            @PathVariable Long id
    ) {

        log.info("DELETE /api/brands/{} - Deleting brand", id);

        brandService.deleteBrand(id);

        return ResponseEntity.noContent().build();
    }
}
