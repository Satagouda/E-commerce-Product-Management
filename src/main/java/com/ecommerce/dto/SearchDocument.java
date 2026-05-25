package com.ecommerce.dto;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchDocument {

    private Long id;

    private String name;

    private String slug;

    private String sku;

    private String shortDescription;

    private String description;

    private String category;

    private String brand;
}