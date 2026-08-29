package com.ndoruhirwe.smartlogistics.mapper;

import com.ndoruhirwe.smartlogistics.dto.response.GoodsReceptionResponse;
import com.ndoruhirwe.smartlogistics.entity.GoodsReception;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GoodsReceptionMapper {

    @Mapping(target = "purchaseOrderId",
            source = "purchaseOrderItem.purchaseOrder.id")

    @Mapping(target = "purchaseOrderNumber",
            source = "purchaseOrderItem.purchaseOrder.orderNumber")

    @Mapping(target = "purchaseOrderItemId", source = "purchaseOrderItem.id")
    @Mapping(target = "productId", source = "purchaseOrderItem.product.id")
    @Mapping(target = "productSku", source = "purchaseOrderItem.product.sku")
    @Mapping(target = "productName", source = "purchaseOrderItem.product.name")

    @Mapping(target = "warehouseId",
            source = "purchaseOrderItem.purchaseOrder.warehouse.id")

    @Mapping(target = "warehouseCode",
            source = "purchaseOrderItem.purchaseOrder.warehouse.code")
    @Mapping(target = "warehouseName",
            source = "purchaseOrderItem.purchaseOrder.warehouse.name")
    GoodsReceptionResponse toResponse(GoodsReception entity);
}
