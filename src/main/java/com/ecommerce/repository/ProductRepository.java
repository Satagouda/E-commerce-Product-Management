package com.ecommerce.repository;


import com.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Find product by slug
     */
    Optional<Product> findBySlug(String slug);

    /**
     * Find product by SKU
     */
    Optional<Product> findBySku(String sku);

    /**
     * Check if slug exists
     */
    boolean existsBySlug(String slug);

    /**
     * Check if SKU exists
     */
    boolean existsBySku(String sku);

    /**
     * Get all ACTIVE products
     */
    Page<Product> findByStatusAndActive(
            Product.ProductStatus status,
            Boolean active,
            Pageable pageable
    );

    /**
     * Get products by category
     */
    Page<Product> findByCategory_IdAndStatusAndActive(
            Long categoryId,
            Product.ProductStatus status,
            Boolean active,
            Pageable pageable
    );

    /**
     * Get products by brand
     */
    Page<Product> findByBrand_IdAndStatusAndActive(
            Long brandId,
            Product.ProductStatus status,
            Boolean active,
            Pageable pageable
    );

    /**
     * Search products
     */
    @Query("""
        SELECT p FROM Product p
        WHERE p.active = true
        AND p.status = 'ACTIVE'
        AND (
            LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(p.shortDescription) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
    """)
    Page<Product> searchProducts(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    /**
     * Filter by category + brand
     */
    @Query("""
        SELECT p FROM Product p
        WHERE p.active = true
        AND p.status = 'ACTIVE'
        AND (:categoryId IS NULL OR p.category.id = :categoryId)
        AND (:brandId IS NULL OR p.brand.id = :brandId)
    """)
    Page<Product> filterProducts(
            @Param("categoryId") Long categoryId,
            @Param("brandId") Long brandId,
            Pageable pageable
    );

    /**
     * Filter by price range
     */
    @Query("""
        SELECT p FROM Product p
        WHERE p.active = true
        AND p.status = 'ACTIVE'
        AND p.salePrice BETWEEN :minPrice AND :maxPrice
    """)
    Page<Product> findByPriceRange(
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable
    );
}
