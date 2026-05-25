package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "image_path", nullable = false)
    private String imagePath; // URL or file path

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0; // Order of display

    @Column(name = "alt_text", length = 200)
    private String altText; // For SEO and accessibility
}

