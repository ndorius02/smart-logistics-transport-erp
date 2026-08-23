package com.ndoruhirwe.smartlogistics.service.implementation;

import com.ndoruhirwe.smartlogistics.dto.request.InventoryCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.InventoryMinimumStockUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.InventoryResponse;
import com.ndoruhirwe.smartlogistics.entity.Inventory;
import com.ndoruhirwe.smartlogistics.entity.Product;
import com.ndoruhirwe.smartlogistics.entity.Warehouse;
import com.ndoruhirwe.smartlogistics.exception.DuplicateResourceException;
import com.ndoruhirwe.smartlogistics.exception.ResourceNotFoundException;
import com.ndoruhirwe.smartlogistics.mapper.InventoryMapper;
import com.ndoruhirwe.smartlogistics.repository.InventoryRepository;
import com.ndoruhirwe.smartlogistics.repository.ProductRepository;
import com.ndoruhirwe.smartlogistics.repository.WarehouseRepository;
import com.ndoruhirwe.smartlogistics.service.InventoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static com.ndoruhirwe.smartlogistics.exception.ErrorMessages.*;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final InventoryMapper inventoryMapper;

    public InventoryServiceImpl(
            InventoryRepository inventoryRepository,
            ProductRepository productRepository,
            WarehouseRepository warehouseRepository,
            InventoryMapper inventoryMapper
    ) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
        this.inventoryMapper = inventoryMapper;
    }

    @Override
    public InventoryResponse createInventory(InventoryCreateRequest request) {
        Product product = findActiveProductById(request.productId());

        Warehouse warehouse = findActiveWarehouseById(request.warehouseId());

        validateUniqueProductWarehouse(request.productId(), request.warehouseId());

        Inventory inventory = inventoryMapper.toEntity(request);

        inventory.setProduct(product);
        inventory.setWarehouse(warehouse);

        inventory.setQuantity(BigDecimal.ZERO);

        inventory.setReservedQuantity(BigDecimal.ZERO);

        inventory.setMinimumStockLevel(request.minimumStockLevel());

        Inventory savedInventory = inventoryRepository.save(inventory);

        return inventoryMapper.toResponse(savedInventory);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InventoryResponse> getAllInventory(Pageable pageable) {
        return inventoryRepository
                .findAll(pageable)
                .map(inventoryMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getInventoryById(UUID id) {
        Inventory inventory = findInventoryById(id);
        return inventoryMapper.toResponse(inventory);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InventoryResponse> getInventoryByWarehouse(UUID warehouseId, Pageable pageable) {
        if (!warehouseRepository.existsById(warehouseId)) {
            throw new ResourceNotFoundException(WAREHOUSE_NOT_FOUND);
        }

        return inventoryRepository
                .findByWarehouseId(warehouseId, pageable)
                .map(inventoryMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InventoryResponse> getInventoryByProduct(UUID productId, Pageable pageable) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException(PRODUCT_NOT_FOUND);
        }

        return inventoryRepository
                .findByProductId(productId, pageable)
                .map(inventoryMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getInventoryByProductAndWarehouse(UUID productId, UUID warehouseId) {
        Inventory inventory =
                inventoryRepository
                        .findByProductIdAndWarehouseId(productId, warehouseId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(INVENTORY_NOT_FOUND));
        return inventoryMapper.toResponse(inventory);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InventoryResponse> getLowStockInventory(Pageable pageable) {
        return inventoryRepository
                .findLowStock(pageable)
                .map(inventoryMapper::toResponse);
    }

    @Override
    public InventoryResponse updateMinimumStockLevel(UUID id,
            InventoryMinimumStockUpdateRequest request) {
        Inventory inventory = findInventoryById(id);

        inventory.setMinimumStockLevel(request.minimumStockLevel());

        Inventory updatedInventory = inventoryRepository.save(inventory);

        return inventoryMapper.toResponse(updatedInventory);
    }

    private Inventory findInventoryById(UUID id) {
        return inventoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                INVENTORY_NOT_FOUND
                        )
                );
    }

    private Product findActiveProductById(UUID productId) {
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

    private Warehouse findActiveWarehouseById(UUID warehouseId) {
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

    private void validateUniqueProductWarehouse(UUID productId, UUID warehouseId) {
        if (
                inventoryRepository
                        .existsByProductIdAndWarehouseId(productId, warehouseId)
        ) {
            throw new DuplicateResourceException(DUPLICATE_INVENTORY_POSITION);
        }
    }
}
