package com.ndoruhirwe.smartlogistics.service.implementation;

import com.ndoruhirwe.smartlogistics.dto.request.StockMovementCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.StockMovementResponse;
import com.ndoruhirwe.smartlogistics.entity.Inventory;
import com.ndoruhirwe.smartlogistics.entity.Product;
import com.ndoruhirwe.smartlogistics.entity.StockMovement;
import com.ndoruhirwe.smartlogistics.entity.Warehouse;
import com.ndoruhirwe.smartlogistics.entity.enums.StockMovementType;
import com.ndoruhirwe.smartlogistics.exception.DuplicateResourceException;
import com.ndoruhirwe.smartlogistics.exception.ResourceNotFoundException;
import com.ndoruhirwe.smartlogistics.mapper.StockMovementMapper;
import com.ndoruhirwe.smartlogistics.repository.InventoryRepository;
import com.ndoruhirwe.smartlogistics.repository.ProductRepository;
import com.ndoruhirwe.smartlogistics.repository.StockMovementRepository;
import com.ndoruhirwe.smartlogistics.repository.WarehouseRepository;
import com.ndoruhirwe.smartlogistics.service.StockMovementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.ndoruhirwe.smartlogistics.exception.ErrorMessages.*;

@Service
@Transactional

public class StockMovementServiceImpl implements StockMovementService  {

    private final StockMovementRepository stockMovementRepository;
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final StockMovementMapper stockMovementMapper;

    public StockMovementServiceImpl(
            StockMovementRepository stockMovementRepository,
            InventoryRepository inventoryRepository,
            ProductRepository productRepository,
            WarehouseRepository warehouseRepository,
            StockMovementMapper stockMovementMapper
    ) {
        this.stockMovementRepository = stockMovementRepository;
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
        this.stockMovementMapper = stockMovementMapper;
    }

    @Override
    public StockMovementResponse createStockMovement(StockMovementCreateRequest request) {
        String normalizedReference = request.reference()
                        .trim()
                        .toUpperCase();
        validateUniqueReference(normalizedReference);
        Product product = findActiveProduct(request.productId());
        Warehouse warehouse = findActiveWarehouse(request.warehouseId());
        Inventory inventory = findInventoryPosition(request.productId(),
                request.warehouseId());
        validateAdjustmentReason(request);

        BigDecimal newQuantity = calculateNewQuantity(inventory, request.movementType(),
                        request.quantity());

        validateInventoryState(inventory, newQuantity);
        inventory.setQuantity(newQuantity);
        inventoryRepository.save(inventory);
        StockMovement stockMovement = stockMovementMapper.toEntity(request);
        stockMovement.setReference(normalizedReference);
        stockMovement.setProduct(product);
        stockMovement.setWarehouse(warehouse);
        stockMovement.setMovementDate(LocalDateTime.now());
        stockMovement.setCreatedBy(getAuthenticatedUsername());
        StockMovement savedMovement = stockMovementRepository.save(stockMovement);
        return stockMovementMapper.toResponse(savedMovement);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StockMovementResponse> getAllStockMovements(Pageable pageable) {
        return stockMovementRepository
                .findAll(pageable)
                .map(stockMovementMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public StockMovementResponse getStockMovementById(UUID id) {
        StockMovement movement = stockMovementRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(STOCK_MOVEMENT_NOT_FOUND)
                        );
        return stockMovementMapper.toResponse(movement);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StockMovementResponse> getStockMovementsByProduct(UUID productId, Pageable pageable) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException(PRODUCT_NOT_FOUND);
        }

        return stockMovementRepository
                .findByProductId(productId, pageable)
                .map(stockMovementMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StockMovementResponse> getStockMovementsByWarehouse(UUID warehouseId,
            Pageable pageable) {
        if (!warehouseRepository.existsById(warehouseId)) {
            throw new ResourceNotFoundException(WAREHOUSE_NOT_FOUND);
        }

        return stockMovementRepository
                .findByWarehouseId(warehouseId, pageable)
                .map(stockMovementMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StockMovementResponse> getStockMovementsByProductAndWarehouse(
            UUID productId,
            UUID warehouseId,
            Pageable pageable) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException(PRODUCT_NOT_FOUND);
        }

        if (!warehouseRepository.existsById(warehouseId)) {
            throw new ResourceNotFoundException(WAREHOUSE_NOT_FOUND);
        }

        return stockMovementRepository
                .findByProductIdAndWarehouseId(productId, warehouseId, pageable)
                .map(stockMovementMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StockMovementResponse> getStockMovementsByType(
            StockMovementType movementType, Pageable pageable) {
        return stockMovementRepository
                .findByMovementType(movementType, pageable)
                .map(stockMovementMapper::toResponse);
    }

    private void validateUniqueReference(String reference) {
        stockMovementRepository
                .findByReferenceIgnoreCase(reference)
                .ifPresent(existing -> {
                    throw new DuplicateResourceException(DUPLICATE_STOCK_MOVEMENT_REFERENCE);
                });
    }

    private Product findActiveProduct(UUID productId) {
        Product product = productRepository
                        .findById(productId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(PRODUCT_NOT_FOUND)
                        );

        if (!product.isActive()) {
            throw new IllegalStateException(PRODUCT_INACTIVE);
        }

        return product;
    }

    private Warehouse findActiveWarehouse(UUID warehouseId) {
        Warehouse warehouse = warehouseRepository
                        .findById(warehouseId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(WAREHOUSE_NOT_FOUND)
                        );

        if (!warehouse.isActive()) {
            throw new IllegalStateException(WAREHOUSE_INACTIVE);
        }

        return warehouse;
    }

    private Inventory findInventoryPosition(UUID productId, UUID warehouseId) {
        return inventoryRepository
                .findByProductIdAndWarehouseId(productId, warehouseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(INVENTORY_NOT_FOUND)
                );
    }

    private void validateAdjustmentReason(
            StockMovementCreateRequest request) {
        boolean adjustment =
                request.movementType()
                        == StockMovementType.ADJUSTMENT_IN ||
                        request.movementType() == StockMovementType.ADJUSTMENT_OUT;

        if (
                adjustment
                        &&
                        (
                                request.reason() == null || request.reason().isBlank()
                        )
        ) {
            throw new IllegalArgumentException(ADJUSTMENT_REASON_REQUIRED);
        }
    }

    private BigDecimal calculateNewQuantity(Inventory inventory,
            StockMovementType movementType, BigDecimal movementQuantity) {
        return switch (movementType) {

            case STOCK_IN,
                 ADJUSTMENT_IN ->
                    inventory
                            .getQuantity()
                            .add(movementQuantity);

            case STOCK_OUT,
                 ADJUSTMENT_OUT ->
                    inventory
                            .getQuantity()
                            .subtract(movementQuantity);
        };
    }

    private void validateInventoryState(Inventory inventory, BigDecimal newQuantity) {
        if (
                newQuantity.compareTo(BigDecimal.ZERO) < 0
        ) {
            throw new IllegalStateException(INSUFFICIENT_STOCK);
        }

        if (
                inventory
                        .getReservedQuantity()
                        .compareTo(newQuantity) > 0
        ) {
            throw new IllegalStateException(RESERVED_STOCK_EXCEEDS_AVAILABLE_STOCK);
        }
    }

    private String getAuthenticatedUsername() {

        Authentication authentication = SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (
                authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException(AUTHENTICATED_USER_REQUIRED);
        }

        return authentication.getName();
    }
}
