package com.ecommerce.service;




import com.ecommerce.config.SearchProperties;
import com.ecommerce.dto.SearchDocument;
import com.ecommerce.dto.SearchResponseDTO;
import com.ecommerce.entity.Product;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.model.SearchResult;
import com.meilisearch.sdk.model.TypoTolerance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.meilisearch.sdk.SearchRequest;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final Client meiliClient;
    private final SearchProperties searchProperties;

    private static final String PRODUCT_INDEX = "products";

    /**
     * Index Product into Meilisearch
     */
    public void indexProduct(Product product) {

        try {

            Index index = meiliClient.index(PRODUCT_INDEX);

            Map<String, Object> document = new HashMap<>();

            document.put("id", product.getId());
            document.put("name", product.getName());
            document.put("slug", product.getSlug());
            document.put("sku", product.getSku());
            document.put("shortDescription", product.getShortDescription());
            document.put("description", product.getDescription());
            document.put("category", product.getCategory().getName());
            document.put("brand", product.getBrand().getName());

            String json = "[" + new com.fasterxml.jackson.databind.ObjectMapper()
                    .writeValueAsString(document) + "]";

            index.addDocuments(json);

            log.info("Product indexed successfully: {}", product.getName());

        } catch (Exception e) {

            log.error("Failed to index product", e);
        }
    }

    /**
     * Delete Product from index
     */
    public void deleteProduct(Long productId) {

        try {

            Index index = meiliClient.index(PRODUCT_INDEX);

            index.deleteDocument(String.valueOf(productId));

            log.info("Product removed from search index");

        } catch (Exception e) {

            log.error("Failed to delete product from Meilisearch", e);
        }
    }

    /**
     * Configure synonyms
     */
    public void configureSynonyms() {

        try {

            Index index = meiliClient.index(PRODUCT_INDEX);

            HashMap<String, String[]> synonymMap = new HashMap<>();

            searchProperties.getSynonyms()
                    .forEach((key, value) -> {

                        synonymMap.put(
                                key,
                                value.split(",")
                        );
                    });

            index.updateSettings(
                    new com.meilisearch.sdk.model.Settings()
                            .setSynonyms(synonymMap)
            );


            log.info("Synonyms configured successfully");

        } catch (Exception e) {

            log.error("Failed to configure synonyms", e);
        }
    }

    /**
     * Search products from Meilisearch
     */
//    public List<HashMap<String, Object>> searchProducts(
//            String keyword
//    ) {
//
//        try {
//
//            Index index = meiliClient.index(PRODUCT_INDEX);
//
//            SearchRequest searchRequest = new SearchRequest(keyword);
//
//            SearchResult result = (SearchResult) index.search(searchRequest);
//
//            return result.getHits();
//
//        } catch (Exception e) {
//
//            log.error("Failed to search products", e);
//
//            return Collections.emptyList();
//        }
//    }
    public SearchResponseDTO searchProducts(
            String keyword
    ) {

        try {

            Index index = meiliClient.index(PRODUCT_INDEX);

            SearchResult result = index.search(keyword);

            List<SearchDocument> documents =
                    result.getHits()
                            .stream()
                            .map(hit -> {

                                HashMap<String, Object> map =
                                        (HashMap<String, Object>) hit;

                                return SearchDocument.builder()
                                        .id(
                                                ((Number) map.get("id")).longValue()
                                        )
                                        .name((String) map.get("name"))
                                        .slug((String) map.get("slug"))
                                        .sku((String) map.get("sku"))
                                        .shortDescription(
                                                (String) map.get("shortDescription")
                                        )
                                        .description(
                                                (String) map.get("description")
                                        )
                                        .category(
                                                (String) map.get("category")
                                        )
                                        .brand(
                                                (String) map.get("brand")
                                        )
                                        .build();
                            })
                            .toList();

            return SearchResponseDTO.builder()
                    .query(keyword)
                    .totalHits(documents.size())
                    .results(documents)
                    .build();

        } catch (Exception e) {

            log.error("Failed to search products", e);

            return SearchResponseDTO.builder()
                    .query(keyword)
                    .totalHits(0)
                    .results(Collections.emptyList())
                    .build();
        }
    }

    public void configureSearchSettings() {

        try {

            Index index = meiliClient.index(PRODUCT_INDEX);

            // Searchable fields
            index.updateSearchableAttributesSettings(
                    new String[]{
                            "name",
                            "shortDescription",
                            "description",
                            "category",
                            "brand"
                    }
            );

            // Filterable fields
            index.updateFilterableAttributesSettings(
                    new String[]{
                            "category",
                            "brand"
                    }
            );

            index.updateTypoToleranceSettings(
                    new TypoTolerance()
                            .setEnabled(true)
            );

            log.info("Search settings configured");

        } catch (Exception e) {

            log.error("Failed to configure search settings", e);
        }
    }
}