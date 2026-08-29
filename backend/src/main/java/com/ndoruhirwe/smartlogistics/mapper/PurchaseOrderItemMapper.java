package com.ndoruhirwe.smartlogistics.mapper;

import com.ndoruhirwe.smartlogistics.dto.response.PurchaseOrderItemResponse;
import com.ndoruhirwe.smartlogistics.entity.PurchaseOrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PurchaseOrderItemMapper {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productSku", source = "product.sku")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "remainingQuantity",
            expression = "java(calculateRemainingQuantity(entity))"
    )
    @Mapping(target = "lineTotal",
            expression = "java(calculateLineTotal(entity))"
    )
    PurchaseOrderItemResponse toResponse(PurchaseOrderItem entity);

    List<PurchaseOrderItemResponse> toResponseList(List<PurchaseOrderItem> entities);

    default BigDecimal calculateRemainingQuantity(PurchaseOrderItem entity) {
        if (entity == null
                || entity.getOrderedQuantity() == null
                || entity.getReceivedQuantity() == null) {
            return BigDecimal.ZERO;
        }

        return entity.getOrderedQuantity()
                .subtract(entity.getReceivedQuantity());
    }

    default BigDecimal calculateLineTotal(PurchaseOrderItem entity) {
        if (entity == null
                || entity.getOrderedQuantity() == null
                || entity.getUnitPrice() == null) {
            return BigDecimal.ZERO;
        }
        return entity.getOrderedQuantity()
                .multiply(entity.getUnitPrice());
    }
}
