package com.ndoruhirwe.smartlogistics.mapper;

import com.ndoruhirwe.smartlogistics.dto.request.SupplierCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.SupplierUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.SupplierResponse;
import com.ndoruhirwe.smartlogistics.entity.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    Supplier toEntity(
            SupplierCreateRequest request
    );

    SupplierResponse toResponse(
            Supplier supplier
    );

    @Mapping(target = "id", ignore = true)
    void updateEntity(
            SupplierUpdateRequest request,
            @MappingTarget Supplier supplier
    );

}
