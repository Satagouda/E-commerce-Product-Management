package com.ecommerce.dto;



import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponseDTO {

    private String query;

    private Integer totalHits;

    private List<SearchDocument> results;
}
