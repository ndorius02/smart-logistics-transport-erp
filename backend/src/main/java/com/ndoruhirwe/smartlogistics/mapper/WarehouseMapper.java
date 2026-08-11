package com.ndoruhirwe.smartlogistics.mapper;

import com.ndoruhirwe.smartlogistics.dto.request.WarehouseCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.WarehouseUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.WarehouseResponse;
import com.ndoruhirwe.smartlogistics.entity.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    Warehouse toEntity(WarehouseCreateRequest request);

    WarehouseResponse toResponse(Warehouse warehouse);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateEntity(
            WarehouseUpdateRequest request,
            @MappingTarget Warehouse warehouse
    );
}
