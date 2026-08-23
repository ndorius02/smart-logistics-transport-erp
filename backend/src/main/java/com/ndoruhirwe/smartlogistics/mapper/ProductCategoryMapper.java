package com.ndoruhirwe.smartlogistics.mapper;

import com.ndoruhirwe.smartlogistics.dto.request.ProductCategoryCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.ProductCategoryUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.ProductCategoryResponse;
import com.ndoruhirwe.smartlogistics.entity.ProductCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductCategoryMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    ProductCategory toEntity(ProductCategoryCreateRequest request);

    ProductCategoryResponse toResponse(ProductCategory category);

    @Mapping(target = "id", ignore = true)
    void updateEntity(
            ProductCategoryUpdateRequest request,
            @MappingTarget ProductCategory category
    );
}
