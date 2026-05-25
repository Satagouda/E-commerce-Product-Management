package com.ecommerce.controller;

import com.ecommerce.ApiResponse;
import com.ecommerce.ApiResponseUtil;
import com.ecommerce.dto.DTOBundles.BrandDTO;
import com.ecommerce.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
@Tag(
        name = "Brands",
        description = "Brand management APIs"
)
public class BrandController {

    private final BrandService brandService;

    /**
     * Get all brands
     */
    @GetMapping
    @Operation(summary = "Get all brands")
    public ResponseEntity<ApiResponse<List<BrandDTO>>> getAllBrands() {

        log.info("GET /api/brands - Fetching all brands");

        List<BrandDTO> brands =
                brandService.getAllBrands();

        return ApiResponseUtil.success(
                "Brands fetched successfully",
                brands
        );
    }

    /**
     * Get brand by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get brand by ID")
    public ResponseEntity<ApiResponse<BrandDTO>> getBrandById(
            @PathVariable Long id
    ) {

        log.info("GET /api/brands/{} - Fetching brand", id);

        BrandDTO brand =
                brandService.getBrandById(id);

        return ApiResponseUtil.success(
                "Brand fetched successfully",
                brand
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
    public ResponseEntity<ApiResponse<BrandDTO>> createBrand(
            @Valid @RequestBody BrandDTO brandDTO
    ) {

        log.info("POST /api/brands - Creating brand: {}", brandDTO.getName());

        BrandDTO createdBrand =
                brandService.createBrand(brandDTO);

        return ApiResponseUtil.created(
                "Brand created successfully",
                createdBrand
        );
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
    public ResponseEntity<ApiResponse<BrandDTO>> updateBrand(
            @PathVariable Long id,
            @Valid @RequestBody BrandDTO brandDTO
    ) {

        log.info("PUT /api/brands/{} - Updating brand", id);

        BrandDTO updatedBrand =
                brandService.updateBrand(id, brandDTO);

        return ApiResponseUtil.success(
                "Brand updated successfully",
                updatedBrand
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
    public ResponseEntity<ApiResponse<Void>> deleteBrand(
            @PathVariable Long id
    ) {

        log.info("DELETE /api/brands/{} - Deleting brand", id);

        brandService.deleteBrand(id);

        return ApiResponseUtil.success(
                "Brand deleted successfully",
                null
        );
    }
}