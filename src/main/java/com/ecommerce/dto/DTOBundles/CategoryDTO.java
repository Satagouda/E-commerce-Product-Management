package com.ecommerce.dto.DTOBundles;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {

    @Schema(example = "1")
    private Long id;

    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
    @Schema(example = "Electronics")
    private String name;

    @NotBlank(message = "Category slug is required")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug must contain only lowercase letters, numbers, and hyphens")
    @Schema(example = "electronics")
    private String slug;

    @Schema(example = "All electronic devices and accessories")
    private String description;

    @URL(message = "Image URL must be a valid URL")
    @Schema(example = "https://cdn.example.com/categories/electronics.jpg")
    private String imageUrl;

    @Schema(example = "0")
    private Integer displayOrder;

    @Schema(example = "150")
    private Integer productCount;
}
