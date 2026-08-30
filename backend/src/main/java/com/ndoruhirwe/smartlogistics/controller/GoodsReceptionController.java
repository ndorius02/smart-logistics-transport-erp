package com.ndoruhirwe.smartlogistics.controller;

import com.ndoruhirwe.smartlogistics.dto.request.GoodsReceptionCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.GoodsReceptionResponse;
import com.ndoruhirwe.smartlogistics.service.GoodsReceptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/goods-receptions")
@RequiredArgsConstructor
public class GoodsReceptionController {

    private final GoodsReceptionService goodsReceptionService;
    @PostMapping
    public ResponseEntity<GoodsReceptionResponse> receive(
            @Valid @RequestBody GoodsReceptionCreateRequest request) {
        GoodsReceptionResponse response = goodsReceptionService.receive(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GoodsReceptionResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(goodsReceptionService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<GoodsReceptionResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(goodsReceptionService.getAll(pageable));
    }

    @GetMapping("/purchase-order/{purchaseOrderId}")
    public ResponseEntity<Page<GoodsReceptionResponse>> getByPurchaseOrder(
            @PathVariable UUID purchaseOrderId, Pageable pageable) {
        return ResponseEntity.ok(goodsReceptionService.getByPurchaseOrder(
                        purchaseOrderId, pageable));
    }

}
