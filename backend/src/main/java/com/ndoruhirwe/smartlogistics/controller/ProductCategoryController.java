package com.ndoruhirwe.smartlogistics.controller;

import com.ndoruhirwe.smartlogistics.dto.request.ProductCategoryCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.ProductCategoryUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.ProductCategoryResponse;
import com.ndoruhirwe.smartlogistics.service.ProductCategoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/product-categories")
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductCategoryResponse createProductCategory(
            @Valid @RequestBody ProductCategoryCreateRequest request) {
        return productCategoryService.createProductCategory(request);
    }

    @GetMapping
    public Page<ProductCategoryResponse> getAllProductCategories(Pageable pageable) {
        return productCategoryService.getAllProductCategories(pageable);
    }
    @GetMapping("/search/code")
    public Page<ProductCategoryResponse> searchByCode(@RequestParam String code, Pageable pageable) {
        return productCategoryService.searchProductCategoriesByCode(code, pageable);
    }

    @GetMapping("/search/name")
    public Page<ProductCategoryResponse> searchByName(@RequestParam String name, Pageable pageable) {
        return productCategoryService.searchProductCategoriesByName(name, pageable);
    }

    @GetMapping("/{id}")
    public ProductCategoryResponse getProductCategoryById(@PathVariable UUID id) {
        return productCategoryService.getProductCategoryById(id);
    }

    @PutMapping("/{id}")
    public ProductCategoryResponse updateProductCategory(@PathVariable UUID id,
            @Valid @RequestBody ProductCategoryUpdateRequest request) {
        return productCategoryService.updateProductCategory(
                id, request);
    }

    @PatchMapping("/{id}/activate")
    public ProductCategoryResponse activateProductCategory(@PathVariable UUID id) {
        return productCategoryService.activateProductCategory(id);
    }

    @PatchMapping("/{id}/deactivate")
    public ProductCategoryResponse deactivateProductCategory(@PathVariable UUID id) {
        return productCategoryService.deactivateProductCategory(id);
    }

}
