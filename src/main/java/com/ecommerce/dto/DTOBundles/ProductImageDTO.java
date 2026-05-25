package com.ecommerce.dto.DTOBundles;



import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageDTO {

    @Schema(example = "1")
    private Long id;

    @NotBlank(message = "Image path is required")
    @Schema(example = "https://cdn.example.com/products/iphone-front.jpg")
    private String imagePath;

    @Schema(example = "0")
    private Integer sortOrder;

    @Schema(example = "Front view of iPhone")
    private String altText;
}