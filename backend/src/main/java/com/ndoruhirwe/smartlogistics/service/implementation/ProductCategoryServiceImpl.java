package com.ndoruhirwe.smartlogistics.service.implementation;

import com.ndoruhirwe.smartlogistics.dto.request.ProductCategoryCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.ProductCategoryUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.ProductCategoryResponse;
import com.ndoruhirwe.smartlogistics.entity.ProductCategory;
import com.ndoruhirwe.smartlogistics.exception.DuplicateResourceException;
import com.ndoruhirwe.smartlogistics.exception.ResourceNotFoundException;
import com.ndoruhirwe.smartlogistics.mapper.ProductCategoryMapper;
import com.ndoruhirwe.smartlogistics.repository.ProductCategoryRepository;
import com.ndoruhirwe.smartlogistics.service.ProductCategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.ndoruhirwe.smartlogistics.exception.ErrorMessages.*;

@Service
@Transactional

public class ProductCategoryServiceImpl implements ProductCategoryService{

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryMapper productCategoryMapper;

    public ProductCategoryServiceImpl(
            ProductCategoryRepository productCategoryRepository,
            ProductCategoryMapper productCategoryMapper
    ) {
        this.productCategoryRepository = productCategoryRepository;
        this.productCategoryMapper = productCategoryMapper;
    }

    @Override
    public ProductCategoryResponse createProductCategory(ProductCategoryCreateRequest request) {

        String normalizedCode = normalizeCode(request.code());

        String normalizedName = normalizeName(request.name());

        validateUniqueCode(
                normalizedCode,
                null
        );

        validateUniqueName(normalizedName, null);

        ProductCategory category = productCategoryMapper.toEntity(request);

        category.setCode(normalizedCode);
        category.setName(normalizedName);
        category.setActive(true);

        ProductCategory savedCategory = productCategoryRepository.save(category);

        return productCategoryMapper.toResponse(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductCategoryResponse> getAllProductCategories(Pageable pageable) {

        return productCategoryRepository
                .findAll(pageable)
                .map(productCategoryMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductCategoryResponse getProductCategoryById(UUID id) {

        ProductCategory category = findProductCategoryById(id);

        return productCategoryMapper.toResponse(category);
    }

    @Override
    public ProductCategoryResponse updateProductCategory(UUID id, ProductCategoryUpdateRequest request) {

        ProductCategory category = findProductCategoryById(id);

        String normalizedCode = normalizeCode(request.code());

        String normalizedName = normalizeName(request.name());

        validateUniqueCode(normalizedCode, id);

        validateUniqueName(normalizedName, id);

        productCategoryMapper.updateEntity(request, category);

        category.setCode(normalizedCode);
        category.setName(normalizedName);

        ProductCategory updatedCategory = productCategoryRepository.save(category);

        return productCategoryMapper.toResponse(updatedCategory);
    }

    @Override
    public ProductCategoryResponse activateProductCategory(UUID id) {

        return changeActiveStatus(id, true);
    }

    @Override
    public ProductCategoryResponse deactivateProductCategory(UUID id) {

        return changeActiveStatus(id, false);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductCategoryResponse> searchProductCategoriesByCode(String code, Pageable pageable) {

        String normalizedCode = normalizeCode(code);

        return productCategoryRepository
                .findByCodeContainingIgnoreCase(normalizedCode, pageable)
                .map(productCategoryMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductCategoryResponse> searchProductCategoriesByName(String name, Pageable pageable) {

        String normalizedName = normalizeName(name);

        return productCategoryRepository
                .findByNameContainingIgnoreCase(normalizedName, pageable)
                .map(productCategoryMapper::toResponse);
    }

    private ProductCategory findProductCategoryById(UUID id) {

        return productCategoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(PRODUCT_CATEGORY_NOT_FOUND)
                );
    }

    private void validateUniqueCode(String code, UUID currentCategoryId) {

        productCategoryRepository
                .findByCodeIgnoreCase(code)
                .filter(existingCategory ->
                        currentCategoryId == null || !existingCategory
                                .getId()
                                .equals(currentCategoryId)
                )
                .ifPresent(existingCategory -> {
                    throw new DuplicateResourceException(DUPLICATE_PRODUCT_CATEGORY_CODE);
                });
    }

    private void validateUniqueName(String name, UUID currentCategoryId) {

        productCategoryRepository
                .findByNameIgnoreCase(name)
                .filter(existingCategory ->
                        currentCategoryId == null || !existingCategory
                                .getId()
                                .equals(currentCategoryId)
                )
                .ifPresent(existingCategory -> {
                    throw new DuplicateResourceException(DUPLICATE_PRODUCT_CATEGORY_NAME);
                });
    }

    private String normalizeCode(String code) {

        return code
                .trim()
                .toUpperCase();
    }

    private String normalizeName(String name) {

        return name.trim();
    }

    private ProductCategoryResponse changeActiveStatus(UUID id, boolean active) {

        ProductCategory category = findProductCategoryById(id);

        if (category.isActive() == active) {
            return productCategoryMapper.toResponse(category);
        }

        category.setActive(active);

        ProductCategory updatedCategory = productCategoryRepository.save(category);

        return productCategoryMapper.toResponse(updatedCategory);
    }

}
