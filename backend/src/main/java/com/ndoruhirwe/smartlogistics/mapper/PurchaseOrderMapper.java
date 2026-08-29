package com.ndoruhirwe.smartlogistics.mapper;

import com.ndoruhirwe.smartlogistics.dto.response.PurchaseOrderItemResponse;
import com.ndoruhirwe.smartlogistics.dto.response.PurchaseOrderResponse;
import com.ndoruhirwe.smartlogistics.entity.PurchaseOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring", uses = PurchaseOrderItemMapper.class)

public interface PurchaseOrderMapper {

    @Mapping(target = "supplierId", source = "supplier.id")
    @Mapping(target = "supplierCode", source = "supplier.code")
    @Mapping(target = "supplierName", source = "supplier.companyName")
    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "warehouseCode", source = "warehouse.code")
    @Mapping(target = "warehouseName", source = "warehouse.name")
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    PurchaseOrderResponse toResponse(PurchaseOrder entity);

    default PurchaseOrderResponse toResponse(
            PurchaseOrder entity,
            List<PurchaseOrderItemResponse> items
    ) {
        PurchaseOrderResponse base = toResponse(entity);

        BigDecimal totalAmount = items == null
                ? BigDecimal.ZERO
                : items.stream()
                .map(PurchaseOrderItemResponse::lineTotal)
                .filter(lineTotal -> lineTotal != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new PurchaseOrderResponse(
                base.id(),
                base.orderNumber(),
                base.supplierId(),
                base.supplierCode(),
                base.supplierName(),
                base.warehouseId(),
                base.warehouseCode(),
                base.warehouseName(),
                base.orderDate(),
                base.expectedDeliveryDate(),
                base.status(),
                base.notes(),
                items == null ? List.of() : items,
                totalAmount,
                base.createdAt(),
                base.updatedAt()
        );
    }

    }
