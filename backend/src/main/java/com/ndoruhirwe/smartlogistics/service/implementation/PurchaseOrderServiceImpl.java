package com.ndoruhirwe.smartlogistics.service.implementation;

import com.ndoruhirwe.smartlogistics.dto.request.PurchaseOrderCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.PurchaseOrderItemCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.PurchaseOrderItemUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.PurchaseOrderUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.PurchaseOrderItemResponse;
import com.ndoruhirwe.smartlogistics.dto.response.PurchaseOrderResponse;
import com.ndoruhirwe.smartlogistics.entity.Product;
import com.ndoruhirwe.smartlogistics.entity.PurchaseOrder;
import com.ndoruhirwe.smartlogistics.entity.PurchaseOrderItem;
import com.ndoruhirwe.smartlogistics.entity.Supplier;
import com.ndoruhirwe.smartlogistics.entity.Warehouse;
import com.ndoruhirwe.smartlogistics.entity.enums.PurchaseOrderStatus;
import com.ndoruhirwe.smartlogistics.exception.BusinessRuleException;
import com.ndoruhirwe.smartlogistics.exception.DuplicateResourceException;
import com.ndoruhirwe.smartlogistics.exception.ResourceNotFoundException;
import com.ndoruhirwe.smartlogistics.mapper.PurchaseOrderItemMapper;
import com.ndoruhirwe.smartlogistics.mapper.PurchaseOrderMapper;
import com.ndoruhirwe.smartlogistics.repository.ProductRepository;
import com.ndoruhirwe.smartlogistics.repository.PurchaseOrderItemRepository;
import com.ndoruhirwe.smartlogistics.repository.PurchaseOrderRepository;
import com.ndoruhirwe.smartlogistics.repository.SupplierRepository;
import com.ndoruhirwe.smartlogistics.repository.WarehouseRepository;
import com.ndoruhirwe.smartlogistics.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static com.ndoruhirwe.smartlogistics.exception.ErrorMessages.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final SupplierRepository supplierRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;
    private final PurchaseOrderItemMapper purchaseOrderItemMapper;

    @Override
    @Transactional
    public PurchaseOrderResponse create(
            PurchaseOrderCreateRequest request
    ) {

        String orderNumber = normalizeOrderNumber(request.orderNumber());

        if (purchaseOrderRepository
                .existsByOrderNumberIgnoreCase(orderNumber)) {

            throw new DuplicateResourceException(DUPLICATE_PURCHASE_ORDER_NUMBER);
        }
        Supplier supplier = getSupplier(request.supplierId());
        validateSupplierActive(supplier);
        Warehouse warehouse = getWarehouse(request.warehouseId());
        validateWarehouseActive(warehouse);
        LocalDate orderDate = LocalDate.now();
        validateExpectedDeliveryDate(orderDate, request.expectedDeliveryDate());
        PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                        .orderNumber(orderNumber)
                        .supplier(supplier)
                        .warehouse(warehouse)
                        .orderDate(orderDate)
                        .expectedDeliveryDate(request.expectedDeliveryDate())
                        .status(PurchaseOrderStatus.DRAFT)
                        .notes(normalizeNullable(request.notes()))
                        .build();
        PurchaseOrder saved = purchaseOrderRepository.save(purchaseOrder);
        return buildResponse(saved);
    }

    @Override
    public PurchaseOrderResponse getById(UUID id) {
        PurchaseOrder purchaseOrder = getPurchaseOrder(id);
        return buildResponse(purchaseOrder);
    }

    @Override
    public Page<PurchaseOrderResponse> getAll(Pageable pageable) {
        return purchaseOrderRepository
                .findAll(pageable)
                .map(this::buildResponse);
    }

    @Override
    public Page<PurchaseOrderResponse> getByStatus(PurchaseOrderStatus status, Pageable pageable) {
        return purchaseOrderRepository
                .findByStatus(status, pageable)
                .map(this::buildResponse);
    }

    @Override
    public Page<PurchaseOrderResponse> getBySupplier(UUID supplierId, Pageable pageable) {
        getSupplier(supplierId);
        return purchaseOrderRepository
                .findBySupplierId(supplierId, pageable)
                .map(this::buildResponse);
    }

    @Override
    public Page<PurchaseOrderResponse> getByWarehouse(UUID warehouseId, Pageable pageable) {
        getWarehouse(warehouseId);
        return purchaseOrderRepository
                .findByWarehouseId(warehouseId, pageable)
                .map(this::buildResponse);
    }

    @Override
    @Transactional
    public PurchaseOrderResponse update(UUID id, PurchaseOrderUpdateRequest request) {
        PurchaseOrder purchaseOrder = getPurchaseOrder(id);
        requireDraft(purchaseOrder);
        Supplier supplier = getSupplier(request.supplierId());
        validateSupplierActive(supplier);
        Warehouse warehouse = getWarehouse(request.warehouseId());
        validateWarehouseActive(warehouse);
        validateExpectedDeliveryDate(purchaseOrder.getOrderDate(), request.expectedDeliveryDate());

        purchaseOrder.setSupplier(supplier);
        purchaseOrder.setWarehouse(warehouse);
        purchaseOrder.setExpectedDeliveryDate(request.expectedDeliveryDate());
        purchaseOrder.setNotes(normalizeNullable(request.notes()));
        PurchaseOrder saved = purchaseOrderRepository.save(purchaseOrder);
        return buildResponse(saved);
    }

    @Override
    @Transactional
    public PurchaseOrderResponse addItem(UUID purchaseOrderId, PurchaseOrderItemCreateRequest request) {

        PurchaseOrder purchaseOrder = getPurchaseOrder(purchaseOrderId);
        requireDraft(purchaseOrder);
        Product product = getProduct(request.productId());
        validateProductActive(product);
        boolean alreadyExists = purchaseOrderItemRepository
                        .existsByPurchaseOrderIdAndProductId(purchaseOrderId, product.getId());

        if (alreadyExists) {
            throw new DuplicateResourceException(DUPLICATE_PURCHASE_ORDER_PRODUCT);
        }

        validateOrderedQuantity(request.orderedQuantity());
        validateUnitPrice(request.unitPrice());

        PurchaseOrderItem item = PurchaseOrderItem.builder()
                        .purchaseOrder(purchaseOrder)
                        .product(product)
                        .orderedQuantity(request.orderedQuantity())
                        .receivedQuantity(BigDecimal.ZERO)
                        .unitPrice(request.unitPrice())
                        .build();

        purchaseOrderItemRepository.save(item);
        return buildResponse(purchaseOrder);
    }

    @Override
    @Transactional
    public PurchaseOrderResponse updateItem(UUID purchaseOrderId, UUID itemId,
            PurchaseOrderItemUpdateRequest request) {
        PurchaseOrder purchaseOrder = getPurchaseOrder(purchaseOrderId);
        requireDraft(purchaseOrder);
        PurchaseOrderItem item = getPurchaseOrderItem(purchaseOrderId, itemId);
        validateOrderedQuantity(request.orderedQuantity());
        validateUnitPrice(request.unitPrice());

        BigDecimal receivedQuantity = item.getReceivedQuantity() == null
                        ? BigDecimal.ZERO
                        : item.getReceivedQuantity();

        if (request.orderedQuantity()
                .compareTo(receivedQuantity) < 0) {

            throw new BusinessRuleException(ORDERED_QUANTITY_BELOW_RECEIVED);
        }

        item.setOrderedQuantity(request.orderedQuantity());
        item.setUnitPrice(request.unitPrice());
        purchaseOrderItemRepository.save(item);
        return buildResponse(purchaseOrder);
    }

    @Override
    @Transactional
    public PurchaseOrderResponse removeItem(UUID purchaseOrderId, UUID itemId) {
        PurchaseOrder purchaseOrder = getPurchaseOrder(purchaseOrderId);
        requireDraft(purchaseOrder);
        PurchaseOrderItem item = getPurchaseOrderItem(purchaseOrderId, itemId);

        BigDecimal receivedQuantity = item.getReceivedQuantity() == null
                        ? BigDecimal.ZERO
                        : item.getReceivedQuantity();

        if (receivedQuantity
                .compareTo(BigDecimal.ZERO) > 0) {

            throw new BusinessRuleException(RECEIVED_ITEM_CANNOT_BE_REMOVED);
        }
        purchaseOrderItemRepository.delete(item);
        purchaseOrderItemRepository.flush();
        return buildResponse(purchaseOrder);
    }

    @Override
    @Transactional
    public PurchaseOrderResponse submit(UUID id) {
        PurchaseOrder purchaseOrder = getPurchaseOrder(id);
        requireDraft(purchaseOrder);
        List<PurchaseOrderItem> items =
                purchaseOrderItemRepository
                        .findByPurchaseOrderId(id);

        if (items.isEmpty()) {

            throw new BusinessRuleException(PURCHASE_ORDER_WITHOUT_ITEMS);
        }

        validateSupplierActive(purchaseOrder.getSupplier());

        validateWarehouseActive(purchaseOrder.getWarehouse());

        for (PurchaseOrderItem item : items) {validateProductActive(item.getProduct());
            validateOrderedQuantity(item.getOrderedQuantity());
            validateUnitPrice(item.getUnitPrice());
        }

        purchaseOrder.setStatus(PurchaseOrderStatus.SUBMITTED);
        PurchaseOrder saved = purchaseOrderRepository.save(purchaseOrder);
        return buildResponse(saved);
    }

    @Override
    @Transactional
    public PurchaseOrderResponse approve(UUID id) {
        PurchaseOrder purchaseOrder = getPurchaseOrder(id);
        if (purchaseOrder.getStatus() != PurchaseOrderStatus.SUBMITTED) {
            throw new BusinessRuleException(PURCHASE_ORDER_MUST_BE_SUBMITTED);
        }

        purchaseOrder.setStatus(PurchaseOrderStatus.APPROVED);
        PurchaseOrder saved = purchaseOrderRepository.save(purchaseOrder);
        return buildResponse(saved);
    }

    @Override
    @Transactional
    public PurchaseOrderResponse cancel(UUID id) {
        PurchaseOrder purchaseOrder = getPurchaseOrder(id);
        PurchaseOrderStatus status = purchaseOrder.getStatus();

        boolean cancellable = status == PurchaseOrderStatus.DRAFT
                        || status == PurchaseOrderStatus.SUBMITTED
                        || status == PurchaseOrderStatus.APPROVED;

        if (!cancellable) {
            throw new BusinessRuleException(PURCHASE_ORDER_CANNOT_BE_CANCELLED);
        }

        List<PurchaseOrderItem> items = purchaseOrderItemRepository
                        .findByPurchaseOrderId(id);
        boolean goodsAlreadyReceived = items.stream()
                        .anyMatch(this::hasReceivedQuantity);
        if (goodsAlreadyReceived) {
            throw new BusinessRuleException(PURCHASE_ORDER_HAS_RECEIVED_GOODS);
        }

        purchaseOrder.setStatus(PurchaseOrderStatus.CANCELLED);
        PurchaseOrder saved = purchaseOrderRepository.save(purchaseOrder);
        return buildResponse(saved);
    }

    private PurchaseOrderResponse buildResponse(PurchaseOrder purchaseOrder) {

        List<PurchaseOrderItem> items = purchaseOrderItemRepository
                        .findByPurchaseOrderId(purchaseOrder.getId());

        List<PurchaseOrderItemResponse> itemResponses =
                purchaseOrderItemMapper
                        .toResponseList(items);

        return purchaseOrderMapper.toResponse(purchaseOrder, itemResponses);
    }

    private PurchaseOrder getPurchaseOrder(UUID id) {
        return purchaseOrderRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(PURCHASE_ORDER_NOT_FOUND)
                );
    }

    private PurchaseOrderItem getPurchaseOrderItem(UUID purchaseOrderId, UUID itemId) {

        return purchaseOrderItemRepository
                .findByIdAndPurchaseOrderId(itemId, purchaseOrderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(PURCHASE_ORDER_ITEM_NOT_FOUND)
                );
    }

    private Supplier getSupplier(UUID id) {
        return supplierRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(SUPPLIER_NOT_FOUND)
                );
    }

    private Warehouse getWarehouse(UUID id) {
        return warehouseRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(WAREHOUSE_NOT_FOUND)
                );
    }

    private Product getProduct(UUID id) {
        return productRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(PRODUCT_NOT_FOUND)
                );
    }

    private void requireDraft(PurchaseOrder purchaseOrder) {
        if (purchaseOrder.getStatus() != PurchaseOrderStatus.DRAFT) {
            throw new BusinessRuleException(PURCHASE_ORDER_MUST_BE_DRAFT);
        }
    }

    private void validateExpectedDeliveryDate(LocalDate orderDate, LocalDate expectedDeliveryDate) {
        if (expectedDeliveryDate != null
                && expectedDeliveryDate.isBefore(orderDate)) {
            throw new BusinessRuleException(EXPECTED_DELIVERY_DATE_INVALID);
        }
    }

    private void validateOrderedQuantity(BigDecimal quantity) {

        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException(ORDERED_QUANTITY_INVALID);
        }
    }

    private void validateUnitPrice(BigDecimal unitPrice) {
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleException(UNIT_PRICE_INVALID);
        }
    }

    private void validateSupplierActive(Supplier supplier) {
        if (!supplier.isActive()) {
            throw new BusinessRuleException(SUPPLIER_INACTIVE);
        }
    }
    private void validateWarehouseActive(Warehouse warehouse) {
        if (!warehouse.isActive()) {
            throw new BusinessRuleException(WAREHOUSE_INACTIVE);
        }
    }

    private void validateProductActive(Product product) {
        if (!product.isActive()) {
            throw new BusinessRuleException(PRODUCT_INACTIVE);
        }
    }

    private boolean hasReceivedQuantity(PurchaseOrderItem item) {
        return item.getReceivedQuantity() != null
                && item.getReceivedQuantity()
                .compareTo(BigDecimal.ZERO) > 0;
    }

    private String normalizeOrderNumber(String orderNumber) {
        if (orderNumber == null || orderNumber.isBlank()) {
            throw new BusinessRuleException("Order number is required");
        }
        return orderNumber
                .trim()
                .toUpperCase(Locale.ROOT);
    }

    private String normalizeNullable(String value) {
        if (value == null || value.isBlank()) {
            return null;}
        return value.trim();
    }
}
