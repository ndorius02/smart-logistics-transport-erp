package com.ndoruhirwe.smartlogistics.mapper;

import com.ndoruhirwe.smartlogistics.dto.request.InventoryCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.InventoryResponse;
import com.ndoruhirwe.smartlogistics.entity.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "quantity", ignore = true)
    @Mapping(target = "reservedQuantity", ignore = true)
    Inventory toEntity(InventoryCreateRequest request);
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productSku", source = "product.sku")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "warehouseCode", source = "warehouse.code")
    @Mapping(target = "warehouseName", source = "warehouse.name")
    @Mapping(target = "availableQuantity", expression = "java(calculateAvailableQuantity(inventory))")
    @Mapping(target = "lowStock", expression = "java(isLowStock(inventory))")
    InventoryResponse toResponse(Inventory inventory);

    default BigDecimal calculateAvailableQuantity(Inventory inventory) {
        return inventory
                .getQuantity()
                .subtract(inventory.getReservedQuantity());
    }

    default boolean isLowStock(Inventory inventory) {
        BigDecimal availableQuantity = calculateAvailableQuantity(inventory);

        return availableQuantity.compareTo(inventory.getMinimumStockLevel()
        ) <= 0;
    }
}
