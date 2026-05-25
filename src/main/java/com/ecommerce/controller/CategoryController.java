package com.ecommerce.controller;

import com.ecommerce.ApiResponse;
import com.ecommerce.ApiResponseUtil;
import com.ecommerce.dto.DTOBundles.CategoryDTO;
import com.ecommerce.service.CategoryService;
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
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(
        name = "Categories",
        description = "Product category management APIs"
)
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Get all categories")
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getAllCategories() {

        log.info("GET /api/categories - Fetching all categories");

        List<CategoryDTO> categories =
                categoryService.getAllCategories();

        return ApiResponseUtil.success(
                "Categories fetched successfully",
                categories
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID")
    public ResponseEntity<ApiResponse<CategoryDTO>> getCategoryById(
            @PathVariable Long id
    ) {
        log.info("GET /api/categories/{} - Fetching category", id);

        CategoryDTO category =
                categoryService.getCategoryById(id);

        return ApiResponseUtil.success(
                "Category fetched successfully",
                category
        );
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get category by slug")
    public ResponseEntity<ApiResponse<CategoryDTO>> getCategoryBySlug(
            @PathVariable String slug
    ) {log.info("GET /api/categories/slug/{} - Fetching category", slug);

        CategoryDTO category =
                categoryService.getCategoryBySlug(slug);

        return ApiResponseUtil.success(
                "Category fetched successfully",
                category
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(
            summary = "Create new category",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ResponseEntity<ApiResponse<CategoryDTO>> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO
    ) {

        log.info("POST /api/categories - Creating category: {}", categoryDTO.getName());

        CategoryDTO createdCategory =
                categoryService.createCategory(categoryDTO);

        return ApiResponseUtil.created(
                "Category created successfully",
                createdCategory
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Update category",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDTO categoryDTO
    ) {log.info("PUT /api/categories/{} - Updating category", id);

        CategoryDTO updatedCategory =
                categoryService.updateCategory(id, categoryDTO);

        return ApiResponseUtil.success(
                "Category updated successfully",
                updatedCategory
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Delete category",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<ApiResponse<Void>> deleteCategory(
            @PathVariable Long id
    ) {

        log.info("DELETE /api/categories/{} - Deleting category", id);

        categoryService.deleteCategory(id);

        return ApiResponseUtil.success(
                "Category deleted successfully",
                null
        );
    }
}