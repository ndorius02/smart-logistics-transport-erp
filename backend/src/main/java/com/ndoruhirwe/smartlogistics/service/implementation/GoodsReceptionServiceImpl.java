package com.ndoruhirwe.smartlogistics.service.implementation;

import com.ndoruhirwe.smartlogistics.dto.request.GoodsReceptionCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.StockMovementCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.GoodsReceptionResponse;
import com.ndoruhirwe.smartlogistics.entity.GoodsReception;
import com.ndoruhirwe.smartlogistics.entity.PurchaseOrder;
import com.ndoruhirwe.smartlogistics.entity.PurchaseOrderItem;
import com.ndoruhirwe.smartlogistics.entity.enums.PurchaseOrderStatus;
import com.ndoruhirwe.smartlogistics.entity.enums.StockMovementType;
import com.ndoruhirwe.smartlogistics.exception.BusinessRuleException;
import com.ndoruhirwe.smartlogistics.exception.DuplicateResourceException;
import com.ndoruhirwe.smartlogistics.exception.ResourceNotFoundException;
import com.ndoruhirwe.smartlogistics.mapper.GoodsReceptionMapper;
import com.ndoruhirwe.smartlogistics.repository.GoodsReceptionRepository;
import com.ndoruhirwe.smartlogistics.repository.InventoryRepository;
import com.ndoruhirwe.smartlogistics.repository.PurchaseOrderItemRepository;
import com.ndoruhirwe.smartlogistics.repository.PurchaseOrderRepository;
import com.ndoruhirwe.smartlogistics.service.GoodsReceptionService;
import com.ndoruhirwe.smartlogistics.service.StockMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import static com.ndoruhirwe.smartlogistics.exception.ErrorMessages.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoodsReceptionServiceImpl implements GoodsReceptionService {

    private final GoodsReceptionRepository goodsReceptionRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final InventoryRepository inventoryRepository;

    private final StockMovementService stockMovementService;
    private final GoodsReceptionMapper goodsReceptionMapper;

    @Override
    @Transactional
    public GoodsReceptionResponse receive(GoodsReceptionCreateRequest request) {
        String reference = normalizeReference(request.reference());
        if (goodsReceptionRepository
                .existsByReferenceIgnoreCase(reference)) {
            throw new DuplicateResourceException(DUPLICATE_GOODS_RECEPTION_REFERENCE);
        }
        validateReceptionQuantity(request.quantity());
        PurchaseOrderItem purchaseOrderItem = purchaseOrderItemRepository
                        .findById(request.purchaseOrderItemId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(PURCHASE_ORDER_ITEM_NOT_FOUND)
                        );
        PurchaseOrder purchaseOrder = purchaseOrderItem.getPurchaseOrder();
        validateReceptionAllowed(purchaseOrder);

        validateRemainingQuantity(purchaseOrderItem, request.quantity());

        UUID productId = purchaseOrderItem
                        .getProduct()
                        .getId();

        UUID warehouseId = purchaseOrder
                        .getWarehouse()
                        .getId();
        validateInventoryPositionExists(productId, warehouseId);
        String authenticatedUser = getAuthenticatedUsername();
        BigDecimal currentReceivedQuantity = purchaseOrderItem
                        .getReceivedQuantity() == null
                        ? BigDecimal.ZERO
                        : purchaseOrderItem
                        .getReceivedQuantity();

        BigDecimal newReceivedQuantity = currentReceivedQuantity
                        .add(request.quantity());
        purchaseOrderItem.setReceivedQuantity(newReceivedQuantity);
        purchaseOrderItemRepository.save(purchaseOrderItem);
        StockMovementCreateRequest stockMovementRequest = new StockMovementCreateRequest(
                        generateStockMovementReference(),
                        productId,
                        warehouseId,
                        StockMovementType.STOCK_IN,
                        request.quantity(),
                        "Goods reception " + reference,
                        normalizeNullable(request.notes())
                );

        stockMovementService.createStockMovement(stockMovementRequest);
        recalculatePurchaseOrderStatus(purchaseOrder);

        GoodsReception goodsReception = GoodsReception.builder()
                        .reference(reference)
                        .purchaseOrderItem(purchaseOrderItem)
                        .quantity(request.quantity())
                        .notes(normalizeNullable(request.notes()))
                        .receptionDate(LocalDateTime.now())
                        .createdBy(authenticatedUser)
                        .build();
        GoodsReception saved = goodsReceptionRepository.save(goodsReception);
        return goodsReceptionMapper.toResponse(saved);
    }

    @Override
    public GoodsReceptionResponse getById(UUID id) {

        GoodsReception goodsReception = goodsReceptionRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(GOODS_RECEPTION_NOT_FOUND));
        return goodsReceptionMapper.toResponse(goodsReception);
    }

    @Override
    public Page<GoodsReceptionResponse> getAll(Pageable pageable) {
        return goodsReceptionRepository
                .findAll(pageable)
                .map(goodsReceptionMapper::toResponse);
    }

    @Override
    public Page<GoodsReceptionResponse> getByPurchaseOrder(UUID purchaseOrderId, Pageable pageable) {
        if (!purchaseOrderRepository
                .existsById(purchaseOrderId)) {
            throw new ResourceNotFoundException(PURCHASE_ORDER_NOT_FOUND);
        }

        return goodsReceptionRepository
                .findByPurchaseOrderItemPurchaseOrderId(purchaseOrderId, pageable)
                .map(goodsReceptionMapper::toResponse);
    }

    private void validateReceptionAllowed(PurchaseOrder purchaseOrder) {

        PurchaseOrderStatus status = purchaseOrder.getStatus();

        boolean receptionAllowed = status == PurchaseOrderStatus.APPROVED
                        || status == PurchaseOrderStatus.PARTIALLY_RECEIVED;

        if (!receptionAllowed) {
            throw new BusinessRuleException(GOODS_RECEPTION_NOT_ALLOWED);
        }
    }

    private void validateReceptionQuantity(BigDecimal quantity) {
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException(GOODS_RECEPTION_QUANTITY_INVALID);
        }
    }

    private void validateRemainingQuantity(PurchaseOrderItem purchaseOrderItem,
            BigDecimal receptionQuantity) {

        BigDecimal orderedQuantity = purchaseOrderItem
                        .getOrderedQuantity();
        BigDecimal receivedQuantity = purchaseOrderItem
                        .getReceivedQuantity() == null
                        ? BigDecimal.ZERO
                        : purchaseOrderItem
                        .getReceivedQuantity();
        BigDecimal newReceivedQuantity = receivedQuantity
                        .add(receptionQuantity);
        if (newReceivedQuantity
                .compareTo(orderedQuantity) > 0) {
            throw new BusinessRuleException(GOODS_RECEPTION_EXCEEDS_ORDERED_QUANTITY);
        }
    }

    private void validateInventoryPositionExists(UUID productId, UUID warehouseId) {

        boolean exists = inventoryRepository
                        .existsByProductIdAndWarehouseId(productId, warehouseId);
        if (!exists) {
            throw new BusinessRuleException(INVENTORY_POSITION_REQUIRED_FOR_RECEPTION);
        }
    }

    private void recalculatePurchaseOrderStatus(PurchaseOrder purchaseOrder) {
        List<PurchaseOrderItem> items = purchaseOrderItemRepository
                        .findByPurchaseOrderId(purchaseOrder.getId());
        boolean allItemsFullyReceived = !items.isEmpty()
                        && items.stream()
                        .allMatch(this::isFullyReceived);

        if (allItemsFullyReceived) {
            purchaseOrder.setStatus(PurchaseOrderStatus.RECEIVED);
        } else {
            purchaseOrder.setStatus(PurchaseOrderStatus.PARTIALLY_RECEIVED);
        }
        purchaseOrderRepository.save(purchaseOrder);
    }

    private boolean isFullyReceived(PurchaseOrderItem item) {
        BigDecimal receivedQuantity = item.getReceivedQuantity() == null
                        ? BigDecimal.ZERO
                        : item.getReceivedQuantity();
        BigDecimal orderedQuantity = item.getOrderedQuantity();
        return receivedQuantity
                .compareTo(orderedQuantity) >= 0;
    }

    private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getName() == null
                || authentication.getName().isBlank()
                || "anonymousUser".equalsIgnoreCase(
                authentication.getName())) {

            throw new BusinessRuleException(AUTHENTICATED_USER_REQUIRED);
        }
        return authentication.getName();
    }

    private String normalizeReference(String reference) {
        if (reference == null || reference.isBlank()) {
            throw new BusinessRuleException(GOODS_RECEPTION_REFERENCE_REQUIRED);
        }
        return reference
                .trim()
                .toUpperCase(Locale.ROOT);
    }

    private String generateStockMovementReference() {
        return "SM-GR-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase(Locale.ROOT);
    }

    private String normalizeNullable(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }











}
