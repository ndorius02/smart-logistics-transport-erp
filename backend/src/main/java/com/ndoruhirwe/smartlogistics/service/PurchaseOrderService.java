package com.ndoruhirwe.smartlogistics.service;

import com.ndoruhirwe.smartlogistics.dto.request.PurchaseOrderCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.PurchaseOrderItemCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.PurchaseOrderItemUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.PurchaseOrderUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.PurchaseOrderResponse;
import com.ndoruhirwe.smartlogistics.entity.enums.PurchaseOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PurchaseOrderService {

    PurchaseOrderResponse create(PurchaseOrderCreateRequest request);

    PurchaseOrderResponse getById(UUID id);

    Page<PurchaseOrderResponse> getAll(Pageable pageable);

    Page<PurchaseOrderResponse> getByStatus(PurchaseOrderStatus status,
            Pageable pageable);

    Page<PurchaseOrderResponse> getBySupplier(UUID supplierId, Pageable pageable);

    Page<PurchaseOrderResponse> getByWarehouse(UUID warehouseId, Pageable pageable);

    PurchaseOrderResponse update(UUID id, PurchaseOrderUpdateRequest request);

    PurchaseOrderResponse addItem(UUID purchaseOrderId,
            PurchaseOrderItemCreateRequest request);

    PurchaseOrderResponse updateItem(UUID purchaseOrderId, UUID itemId,
            PurchaseOrderItemUpdateRequest request);

    PurchaseOrderResponse removeItem(UUID purchaseOrderId, UUID itemId);

    PurchaseOrderResponse submit(UUID id);

    PurchaseOrderResponse approve(UUID id);

    PurchaseOrderResponse cancel(UUID id);
}
