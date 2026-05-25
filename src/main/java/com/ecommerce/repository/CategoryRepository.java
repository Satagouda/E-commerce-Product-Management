package com.ecommerce.repository;

import com.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    /**
     * Find category by slug
     */
    Optional<Category> findBySlug(String slug);
    
    /**
     * Find all active categories ordered by display order
     */
    @Query("SELECT c FROM Category c WHERE c.active = true ORDER BY c.displayOrder ASC")
    List<Category> findAllActive();
    
    /**
     * Check if category slug exists
     */
    boolean existsBySlug(String slug);
    
    /**
     * Find categories with products
     */
    @Query("SELECT c FROM Category c WHERE c.productCount > 0 ORDER BY c.displayOrder ASC")
    List<Category> findCategoriesWithProducts();
}
