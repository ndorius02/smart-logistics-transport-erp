package com.ndoruhirwe.smartlogistics.service;

import com.ndoruhirwe.smartlogistics.dto.request.GoodsReceptionCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.GoodsReceptionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface GoodsReceptionService {

    GoodsReceptionResponse receive(GoodsReceptionCreateRequest request);

    GoodsReceptionResponse getById(UUID id);

    Page<GoodsReceptionResponse> getAll(Pageable pageable);

    Page<GoodsReceptionResponse> getByPurchaseOrder(UUID purchaseOrderId, Pageable pageable);
}
