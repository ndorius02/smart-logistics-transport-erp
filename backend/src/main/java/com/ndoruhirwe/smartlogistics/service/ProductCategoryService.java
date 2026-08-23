package com.ndoruhirwe.smartlogistics.service;

import com.ndoruhirwe.smartlogistics.dto.request.ProductCategoryCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.ProductCategoryUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.ProductCategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductCategoryService {

    ProductCategoryResponse createProductCategory(ProductCategoryCreateRequest request);

    Page<ProductCategoryResponse> getAllProductCategories(Pageable pageable);

    ProductCategoryResponse getProductCategoryById(UUID id);

    ProductCategoryResponse updateProductCategory(UUID id, ProductCategoryUpdateRequest request);

    ProductCategoryResponse activateProductCategory(UUID id);

    ProductCategoryResponse deactivateProductCategory(UUID id);

    Page<ProductCategoryResponse> searchProductCategoriesByCode(String code, Pageable pageable);

    Page<ProductCategoryResponse> searchProductCategoriesByName(String name, Pageable pageable);
}
