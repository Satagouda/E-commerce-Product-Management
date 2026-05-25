package com.ecommerce.dto;


import com.ecommerce.dto.DTOBundles.ProductImageDTO;
import com.ecommerce.dto.DTOBundles.ProductVariantDTO;
import com.ecommerce.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    @Schema(example = "1")
    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(max = 200)
    @Schema(example = "iPhone 15 Pro")
    private String name;

    @NotBlank(message = "Slug is required")
    @Pattern(
            regexp = "^[a-z0-9-]+$",
            message = "Slug must contain lowercase letters, numbers and hyphens"
    )
    @Schema(example = "iphone-15-pro")
    private String slug;

    @NotBlank(message = "SKU is required")
    @Schema(example = "IPHONE15PRO")
    private String sku;

    @Size(max = 500)
    @Schema(example = "Latest Apple iPhone")
    private String shortDescription;

    @Schema(example = "Apple flagship smartphone")
    private String description;

    @NotNull(message = "Regular price is required")
    @DecimalMin(value = "0.0")
    @Schema(example = "99999")
    private BigDecimal regularPrice;

    @DecimalMin(value = "0.0")
    @Schema(example = "94999")
    private BigDecimal salePrice;

    @DecimalMin(value = "0.0")
    @Schema(example = "18")
    private BigDecimal taxPercentage;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0)
    @Schema(example = "20")
    private Integer stockQuantity;

    @Schema(example = "https://cdn.example.com/products/iphone15.jpg")
    private String featuredImage;

    @NotNull(message = "Status is required")
    private Product.ProductStatus status;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    private String categoryName;

    @NotNull(message = "Brand ID is required")
    private Long brandId;

    private String brandName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<ProductVariantDTO> variants;

    private List<ProductImageDTO> images;
}
