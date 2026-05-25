package com.ecommerce.service;


import com.ecommerce.dto.ProductDTO;
import com.ecommerce.entity.*;
import com.ecommerce.exception.customException.ResourceNotFoundException;

import com.ecommerce.repository.Bundle.BrandRepository;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final SearchService searchService;

    /**
     * Create Product
     */
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {

        log.info("Creating product: {}", productDTO.getName());

        if (productRepository.existsBySlug(productDTO.getSlug())) {
            throw new IllegalArgumentException(
                    "Product with slug already exists"
            );
        }

        if (productRepository.existsBySku(productDTO.getSku())) {
            throw new IllegalArgumentException(
                    "Product with SKU already exists"
            );
        }

        Category category = categoryRepository.findById(
                productDTO.getCategoryId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Category not found with ID: "
                                + productDTO.getCategoryId()
                )
        );

        Brand brand = brandRepository.findById(
                productDTO.getBrandId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Brand not found with ID: "
                                + productDTO.getBrandId()
                )
        );

        Product product = Product.builder()
                .name(productDTO.getName())
                .slug(productDTO.getSlug())
                .sku(productDTO.getSku())
                .shortDescription(productDTO.getShortDescription())
                .description(productDTO.getDescription())
                .regularPrice(productDTO.getRegularPrice())
                .salePrice(productDTO.getSalePrice())
                .taxPercentage(productDTO.getTaxPercentage())
                .stockQuantity(productDTO.getStockQuantity())
                .featuredImage(productDTO.getFeaturedImage())
                .status(productDTO.getStatus())
                .category(category)
                .brand(brand)
                .build();

        if (productDTO.getVariants() != null) {

            Set<ProductVariant> variants = productDTO.getVariants()
                    .stream()
                    .map(variantDTO -> {

                        ProductVariant variant = ProductVariant.builder()
                                .variantName(variantDTO.getVariantName())
                                .variantValue(variantDTO.getVariantValue())
                                .sku(variantDTO.getSku())
                                .price(variantDTO.getPrice())
                                .stock(variantDTO.getStock())
                                .product(product)
                                .build();

                        return variant;
                    })
                    .collect(Collectors.toSet());

            product.setVariants(variants);
        }
        if (productDTO.getImages() != null) {

            Set<ProductImage> images = productDTO.getImages()
                    .stream()
                    .map(imageDTO -> {

                        ProductImage image = ProductImage.builder()
                                .imagePath(imageDTO.getImagePath())
                                .sortOrder(imageDTO.getSortOrder())
                                .altText(imageDTO.getAltText())
                                .product(product)
                                .build();

                        return image;
                    })
                    .collect(Collectors.toSet());

            product.setImages(images);
        }
        Product savedProduct = productRepository.save(product);
        searchService.indexProduct(savedProduct);
        log.info(
                "Product created successfully with ID: {}",
                savedProduct.getId()
        );

        return convertToDTO(savedProduct);
    }

    /**
     * Get all products
     */
    public Page<ProductDTO> getAllProducts(
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return productRepository
                .findByStatusAndActive(
                        Product.ProductStatus.ACTIVE,
                        true,
                        pageable
                )
                .map(this::convertToDTO);
    }

    /**
     * Get product by ID
     */
    public ProductDTO getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with ID: " + id
                        )
                );

        return convertToDTO(product);
    }

    /**
     * Update Product
     */
    @Transactional
    public ProductDTO updateProduct(
            Long id,
            ProductDTO productDTO
    ) {

        log.info("Updating product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with ID: " + id
                        )
                );

        Category category = categoryRepository.findById(
                productDTO.getCategoryId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Category not found with ID: "
                                + productDTO.getCategoryId()
                )
        );

        Brand brand = brandRepository.findById(
                productDTO.getBrandId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Brand not found with ID: "
                                + productDTO.getBrandId()
                )
        );

        product.setName(productDTO.getName());
        product.setSlug(productDTO.getSlug());
        product.setSku(productDTO.getSku());
        product.setShortDescription(productDTO.getShortDescription());
        product.setDescription(productDTO.getDescription());
        product.setRegularPrice(productDTO.getRegularPrice());
        product.setSalePrice(productDTO.getSalePrice());
        product.setTaxPercentage(productDTO.getTaxPercentage());
        product.setStockQuantity(productDTO.getStockQuantity());
        product.setFeaturedImage(productDTO.getFeaturedImage());
        product.setStatus(productDTO.getStatus());
        product.setCategory(category);
        product.setBrand(brand);

        Product updatedProduct = productRepository.save(product);
        searchService.indexProduct(updatedProduct);
        log.info(
                "Product updated successfully with ID: {}",
                updatedProduct.getId()
        );

        return convertToDTO(updatedProduct);
    }

    /**
     * Delete Product (Soft Delete)
     */
    @Transactional
    public void deleteProduct(Long id) {

        log.info("Deleting product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with ID: " + id
                        )
                );

        product.setActive(false);

        productRepository.save(product);
        searchService.deleteProduct(id);
        log.info("Product deleted successfully");
    }

    /**
     * Get products by category
     */
    public Page<ProductDTO> getProductsByCategory(
            Long categoryId,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return productRepository
                .findByCategory_IdAndStatusAndActive(
                        categoryId,
                        Product.ProductStatus.ACTIVE,
                        true,
                        pageable
                )
                .map(this::convertToDTO);
    }

    /**
     * Get products by brand
     */
    public Page<ProductDTO> getProductsByBrand(
            Long brandId,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return productRepository
                .findByBrand_IdAndStatusAndActive(
                        brandId,
                        Product.ProductStatus.ACTIVE,
                        true,
                        pageable
                )
                .map(this::convertToDTO);
    }

    /**
     * Search products
     */
    public Page<ProductDTO> searchProducts(
            String keyword,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return productRepository
                .searchProducts(keyword, pageable)
                .map(this::convertToDTO);
    }

    /**
     * Convert Entity -> DTO
     */
    private ProductDTO convertToDTO(Product product) {

        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .sku(product.getSku())
                .shortDescription(product.getShortDescription())
                .description(product.getDescription())
                .regularPrice(product.getRegularPrice())
                .salePrice(product.getSalePrice())
                .taxPercentage(product.getTaxPercentage())
                .stockQuantity(product.getStockQuantity())
                .featuredImage(product.getFeaturedImage())
                .status(product.getStatus())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .brandId(product.getBrand().getId())
                .brandName(product.getBrand().getName())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}