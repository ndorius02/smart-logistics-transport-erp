package com.ndoruhirwe.smartlogistics.service;

import com.ndoruhirwe.smartlogistics.dto.request.ProductCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.ProductUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {

    ProductResponse createProduct(ProductCreateRequest request);

    Page<ProductResponse> getAllProducts(Pageable pageable);

    ProductResponse getProductById(UUID id);

    ProductResponse updateProduct(UUID id, ProductUpdateRequest request);

    ProductResponse activateProduct(UUID id);

    ProductResponse deactivateProduct(UUID id);

    Page<ProductResponse> searchProductsBySku(String sku, Pageable pageable);

    Page<ProductResponse> searchProductsByName(String name, Pageable pageable);

    Page<ProductResponse> getProductsByCategory(UUID categoryId, Pageable pageable);
}
