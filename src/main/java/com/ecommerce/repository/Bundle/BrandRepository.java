package com.ecommerce.repository.Bundle;

import com.ecommerce.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    Optional<Brand> findBySlug(String slug);

    @Query("SELECT b FROM Brand b WHERE b.active = true ORDER BY b.name ASC")
    List<Brand> findAllActive();

    boolean existsBySlug(String slug);
}
