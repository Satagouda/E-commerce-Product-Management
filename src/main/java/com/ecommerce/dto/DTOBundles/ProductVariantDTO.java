package com.ecommerce.dto.DTOBundles;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantDTO {

    @Schema(example = "1")
    private Long id;

    @NotBlank(message = "Variant name is required")
    @Schema(example = "Color")
    private String variantName;

    @NotBlank(message = "Variant value is required")
    @Schema(example = "Red")
    private String variantValue;

    @NotBlank(message = "SKU is required")
    @Schema(example = "IPHONE15-RED")
    private String sku;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0")
    @Schema(example = "99999")
    private BigDecimal price;

    @NotNull(message = "Stock is required")
    @Min(value = 0)
    @Schema(example = "10")
    private Integer stock;
}