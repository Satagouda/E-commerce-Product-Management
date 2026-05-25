package com.ecommerce.service;

import com.ecommerce.dto.DTOBundles.BrandDTO;
import com.ecommerce.entity.Brand;
import com.ecommerce.exception.customException.ResourceNotFoundException;
import com.ecommerce.repository.Bundle.BrandRepository;
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
public class BrandService {

    private final BrandRepository brandRepository;

    /**
     * Get all brands
     */
//    @Cacheable(value = "brands", key = "'all'")
    public List<BrandDTO> getAllBrands() {
        log.debug("Fetching all brands");
        return brandRepository.findAllActive().stream()
                .map(this::convertToDTO)
                .toList();
    }

    /**
     * Get brand by ID
     */
//    @Cacheable(value = "brands", key = "#id")
    public BrandDTO getBrandById(Long id) {
        log.debug("Fetching brand with ID: {}", id);
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with ID: " + id));
        return convertToDTO(brand);
    }

    /**
     * Get brand by slug
     */
//    @Cacheable(value = "brandsBySlug", key = "#slug")
    public BrandDTO getBrandBySlug(String slug) {
        log.debug("Fetching brand with slug: {}", slug);
        Brand brand = brandRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with slug: " + slug));
        return convertToDTO(brand);
    }

    /**
     * Create new brand
     */
    @Transactional
//    @CacheEvict(value = {"brands", "brandsBySlug"}, allEntries = true)
    public BrandDTO createBrand(BrandDTO brandDTO) {
        log.info("Creating new brand: {}", brandDTO.getName());

        if (brandRepository.existsBySlug(brandDTO.getSlug())) {
            throw new IllegalArgumentException("Brand with slug " + brandDTO.getSlug() + " already exists");
        }

        Brand brand = Brand.builder()
                .name(brandDTO.getName())
                .slug(brandDTO.getSlug())
                .description(brandDTO.getDescription())
                .logoUrl(brandDTO.getLogoUrl())
                .website(brandDTO.getWebsite())
                .build();

        brand = brandRepository.save(brand);
        log.info("Brand created with ID: {}", brand.getId());

        return convertToDTO(brand);
    }

    /**
     * Update brand
     */
    @Transactional
//    @CacheEvict(value = {"brands", "brandsBySlug"}, allEntries = true)
    public BrandDTO updateBrand(Long id, BrandDTO brandDTO) {
        log.info("Updating brand with ID: {}", id);

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with ID: " + id));

        brand.setName(brandDTO.getName());
        brand.setSlug(brandDTO.getSlug());
        brand.setDescription(brandDTO.getDescription());
        brand.setLogoUrl(brandDTO.getLogoUrl());
        brand.setWebsite(brandDTO.getWebsite());

        brand = brandRepository.save(brand);
        log.info("Brand updated with ID: {}", id);

        return convertToDTO(brand);
    }

    /**
     * Delete brand
     */
    @Transactional
//    @CacheEvict(value = {"brands", "brandsBySlug"}, allEntries = true)
    public void deleteBrand(Long id) {
        log.info("Deleting brand with ID: {}", id);

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with ID: " + id));

        brand.setActive(false);
        brandRepository.save(brand);

        log.info("Brand deleted with ID: {}", id);
    }

    private BrandDTO convertToDTO(Brand brand) {
        return BrandDTO.builder()
                .id(brand.getId())
                .name(brand.getName())
                .slug(brand.getSlug())
                .description(brand.getDescription())
                .logoUrl(brand.getLogoUrl())
                .website(brand.getWebsite())
                .productCount(brand.getProductCount())
                .build();
    }
}
