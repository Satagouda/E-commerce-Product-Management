package com.ecommerce.service;

import com.ecommerce.dto.DTOBundles.CategoryDTO;
import com.ecommerce.entity.Category;
import com.ecommerce.exception.customException.ResourceNotFoundException;
import com.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    /**
     * Get all categories
     */
    @Cacheable(value = "categories", key = "'all'")
    public List<CategoryDTO> getAllCategories() {
        log.debug("Fetching all categories");
        return categoryRepository.findAllActive().stream()
            .map(this::convertToDTO)
            .toList();
    }
    
    /**
     * Get category by ID
     */
    @Cacheable(value = "categories", key = "#id")
    public CategoryDTO getCategoryById(Long id) {
        log.debug("Fetching category with ID: {}", id);
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
        return convertToDTO(category);
    }
    
    /**
     * Get category by slug
     */
    @Cacheable(value = "categoriesBySlug", key = "#slug")
    public CategoryDTO getCategoryBySlug(String slug) {
        log.debug("Fetching category with slug: {}", slug);
        Category category = categoryRepository.findBySlug(slug)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with slug: " + slug));
        return convertToDTO(category);
    }
    
    /**
     * Create new category
     */
    @Transactional
    @CacheEvict(value = {"categories", "categoriesBySlug"}, allEntries = true)
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        log.info("Creating new category: {}", categoryDTO.getName());
        
        if (categoryRepository.existsBySlug(categoryDTO.getSlug())) {
            throw new IllegalArgumentException("Category with slug " + categoryDTO.getSlug() + " already exists");
        }
        
        Category category = Category.builder()
            .name(categoryDTO.getName())
            .slug(categoryDTO.getSlug())
            .description(categoryDTO.getDescription())
            .imageUrl(categoryDTO.getImageUrl())
            .displayOrder(categoryDTO.getDisplayOrder())
            .build();
        
        category = categoryRepository.save(category);
        log.info("Category created with ID: {}", category.getId());
        
        return convertToDTO(category);
    }
    
    /**
     * Update category
     */
    @Transactional
    @CacheEvict(value = {"categories", "categoriesBySlug"}, allEntries = true)
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        log.info("Updating category with ID: {}", id);
        
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
        
        category.setName(categoryDTO.getName());
        category.setSlug(categoryDTO.getSlug());
        category.setDescription(categoryDTO.getDescription());
        category.setImageUrl(categoryDTO.getImageUrl());
        category.setDisplayOrder(categoryDTO.getDisplayOrder());
        
        category = categoryRepository.save(category);
        log.info("Category updated with ID: {}", id);
        
        return convertToDTO(category);
    }
    
    /**
     * Delete category
     */
    @Transactional
    @CacheEvict(value = {"categories", "categoriesBySlug"}, allEntries = true)
    public void deleteCategory(Long id) {
        log.info("Deleting category with ID: {}", id);
        
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
        
        category.setActive(false);
        categoryRepository.save(category);
        
        log.info("Category deleted with ID: {}", id);
    }
    
    private CategoryDTO convertToDTO(Category category) {
        return CategoryDTO.builder()
            .id(category.getId())
            .name(category.getName())
            .slug(category.getSlug())
            .description(category.getDescription())
            .imageUrl(category.getImageUrl())
            .displayOrder(category.getDisplayOrder())
            .productCount(category.getProductCount())
            .build();
    }
}

