package com.ndoruhirwe.smartlogistics.mapper;

import com.ndoruhirwe.smartlogistics.dto.request.StockMovementCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.StockMovementResponse;
import com.ndoruhirwe.smartlogistics.entity.StockMovement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockMovementMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "movementDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    StockMovement toEntity(StockMovementCreateRequest request);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productSku", source = "product.sku")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "warehouseCode", source = "warehouse.code")
    @Mapping(target = "warehouseName", source = "warehouse.name")
    StockMovementResponse toResponse(StockMovement stockMovement);
}
