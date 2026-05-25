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
public class BrandDTO {

    @Schema(example = "1")
    private Long id;

    @NotBlank(message = "Brand name is required")
    @Size(min = 2, max = 100, message = "Brand name must be between 2 and 100 characters")
    @Schema(example = "Sony")
    private String name;

    @NotBlank(message = "Brand slug is required")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug must contain only lowercase letters, numbers, and hyphens")
    @Schema(example = "sony")
    private String slug;

    @Schema(example = "Sony is a Japanese multinational conglomerate corporation")
    private String description;

    @URL(message = "Logo URL must be a valid URL")
    @Schema(example = "https://cdn.example.com/brands/sony-logo.png")
    private String logoUrl;

    @URL(message = "Website must be a valid URL")
    @Schema(example = "https://www.sony.com")
    private String website;

    @Schema(example = "500")
    private Integer productCount;
}
