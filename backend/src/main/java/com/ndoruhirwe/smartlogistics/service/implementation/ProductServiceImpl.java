package com.ndoruhirwe.smartlogistics.service.implementation;

import com.ndoruhirwe.smartlogistics.dto.request.ProductCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.ProductUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.ProductResponse;
import com.ndoruhirwe.smartlogistics.entity.Product;
import com.ndoruhirwe.smartlogistics.entity.ProductCategory;
import com.ndoruhirwe.smartlogistics.exception.DuplicateResourceException;
import com.ndoruhirwe.smartlogistics.exception.ResourceNotFoundException;
import com.ndoruhirwe.smartlogistics.mapper.ProductMapper;
import com.ndoruhirwe.smartlogistics.repository.ProductCategoryRepository;
import com.ndoruhirwe.smartlogistics.repository.ProductRepository;
import com.ndoruhirwe.smartlogistics.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.ndoruhirwe.smartlogistics.exception.ErrorMessages.*;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(
            ProductRepository productRepository,
            ProductCategoryRepository productCategoryRepository,
            ProductMapper productMapper
    ) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductResponse createProduct(ProductCreateRequest request) {
        String normalizedSku = normalizeSku(request.sku());

        validateUniqueSku(normalizedSku, null);

        ProductCategory category = findActiveCategoryById(request.categoryId());

        Product product = productMapper.toEntity(request);

        product.setSku(normalizedSku);
        product.setCategory(category);
        product.setActive(true);

        Product savedProduct = productRepository.save(product);

        return productMapper.toResponse(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository
                .findAll(pageable)
                .map(productMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(UUID id) {
        Product product = findProductById(id);
        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse updateProduct(UUID id, ProductUpdateRequest request) {
        Product product = findProductById(id);

        String normalizedSku = normalizeSku(request.sku());

        validateUniqueSku(normalizedSku, id);

        ProductCategory category = findActiveCategoryById(request.categoryId());

        productMapper.updateEntity(request, product);

        product.setSku(normalizedSku);
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);

        return productMapper.toResponse(updatedProduct);
    }

    @Override
    public ProductResponse activateProduct(UUID id) {
        return changeActiveStatus(id, true);
    }

    @Override
    public ProductResponse deactivateProduct(UUID id) {
        return changeActiveStatus(id, false);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProductsBySku(String sku, Pageable pageable) {
        String normalizedSku = normalizeSku(sku);

        return productRepository
                .findBySkuContainingIgnoreCase(normalizedSku, pageable)
                .map(productMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProductsByName(String name, Pageable pageable) {
        String normalizedName = name.trim();

        return productRepository
                .findByNameContainingIgnoreCase(normalizedName, pageable)
                .map(productMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByCategory(UUID categoryId, Pageable pageable) {
        if (!productCategoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException(PRODUCT_CATEGORY_NOT_FOUND);
        }

        return productRepository
                .findByCategoryId(categoryId, pageable)
                .map(productMapper::toResponse);
    }

    private Product findProductById(UUID id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND));
    }

    private ProductCategory findActiveCategoryById(UUID categoryId) {
        ProductCategory category =
                productCategoryRepository
                        .findById(categoryId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(PRODUCT_CATEGORY_NOT_FOUND));

        if (!category.isActive()) {
            throw new IllegalStateException(PRODUCT_CATEGORY_INACTIVE);
        }

        return category;
    }

    private void validateUniqueSku(String sku, UUID currentProductId) {
        productRepository
                .findBySkuIgnoreCase(sku)
                .filter(existingProduct -> currentProductId == null
                                || !existingProduct
                                .getId()
                                .equals(currentProductId)
                )
                .ifPresent(existingProduct -> {
                    throw new DuplicateResourceException(DUPLICATE_PRODUCT_SKU);
                });
    }

    private String normalizeSku(String sku) {
        return sku
                .trim()
                .toUpperCase();
    }

    private ProductResponse changeActiveStatus(UUID id, boolean active) {
        Product product = findProductById(id);

        if (product.isActive() == active) {
            return productMapper.toResponse(product);
        }

        product.setActive(active);

        Product updatedProduct = productRepository.save(product);

        return productMapper.toResponse(updatedProduct);
    }
}
