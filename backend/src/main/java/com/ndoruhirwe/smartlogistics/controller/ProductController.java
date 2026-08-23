package com.ndoruhirwe.smartlogistics.controller;

import com.ndoruhirwe.smartlogistics.dto.request.ProductCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.ProductUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.ProductResponse;
import com.ndoruhirwe.smartlogistics.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(
            @Valid @RequestBody ProductCreateRequest request) {
        return productService.createProduct(request);
    }

    @GetMapping
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productService.getAllProducts(pageable);
    }

    @GetMapping("/search/sku")
    public Page<ProductResponse> searchBySku(@RequestParam String sku, Pageable pageable) {
        return productService.searchProductsBySku(sku, pageable);
    }

    @GetMapping("/search/name")
    public Page<ProductResponse> searchByName(@RequestParam String name,
            Pageable pageable) {
        return productService.searchProductsByName(name, pageable);
    }

    @GetMapping("/category/{categoryId}")
    public Page<ProductResponse> getProductsByCategory(@PathVariable UUID categoryId,
            Pageable pageable) {
        return productService.getProductsByCategory(categoryId, pageable);
    }

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable UUID id) {
        return productService.getProductById(id);
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(@PathVariable UUID id,
            @Valid @RequestBody ProductUpdateRequest request) {
        return productService.updateProduct(id, request);
    }

    @PatchMapping("/{id}/activate")
    public ProductResponse activateProduct(@PathVariable UUID id) {
        return productService.activateProduct(id);
    }

    @PatchMapping("/{id}/deactivate")
    public ProductResponse deactivateProduct(@PathVariable UUID id) {
        return productService.deactivateProduct(id);
    }
}
