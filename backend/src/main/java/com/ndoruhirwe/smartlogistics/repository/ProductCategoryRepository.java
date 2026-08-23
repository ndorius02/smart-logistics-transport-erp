package com.ndoruhirwe.smartlogistics.repository;

import com.ndoruhirwe.smartlogistics.entity.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, UUID> {

    Optional<ProductCategory> findByCodeIgnoreCase(String code);

    Optional<ProductCategory> findByNameIgnoreCase(String name);

    Page<ProductCategory> findByCodeContainingIgnoreCase(String code, Pageable pageable);

    Page<ProductCategory> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
