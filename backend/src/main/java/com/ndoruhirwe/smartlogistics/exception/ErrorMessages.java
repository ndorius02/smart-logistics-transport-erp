package com.ndoruhirwe.smartlogistics.exception;

public final class ErrorMessages {
    private ErrorMessages() {
    }

    public static final String USER_NOT_FOUND =
            "User not found";

    public static final String DUPLICATE_USER_EMAIL =
            "A user with this email already exists";

    public static final String ROLE_NOT_FOUND =
            "Role not found";
    public static final String DUPLICATE_ROLE_NAME =
            "Role already exists";

    public static final String WAREHOUSE_NOT_FOUND =
            "Warehouse not found";
    public static final String DUPLICATE_WAREHOUSE_CODE =
            "A warehouse with this code already exists";

    public static final String VEHICLE_NOT_FOUND =
            "Vehicle not found";
    public static final String DUPLICATE_VEHICLE_REGISTRATION =
            "A vehicle with this registration number already exists";

    public static final String DRIVER_NOT_FOUND =
            "Driver not found";
    public static final String DUPLICATE_DRIVER_LICENSE =
            "A driver with this license number already exists";

    public static final String TRANSPORT_NOT_FOUND =
            "Transport not found";
    public static final String DUPLICATE_TRANSPORT_CODE =
            "A transport with this code already exists";
    public static final String ORIGIN_DESTINATION_MUST_DIFFER =
            "Origin and destination warehouses must be different";
    public static final String WAREHOUSE_INACTIVE =
            "Warehouse must be active";
    public static final String VEHICLE_NOT_AVAILABLE =
            "Vehicle is not available";
    public static final String DRIVER_NOT_AVAILABLE =
            "Driver is not available";
    public static final String INVALID_TRANSPORT_DATES =
            "Planned departure must be before planned arrival";
    public static final String INVALID_TRANSPORT_STATUS_TRANSITION =
            "Invalid transport status transition";

    public static final String CUSTOMER_NOT_FOUND =
            "Customer not found";

    public static final String DUPLICATE_CUSTOMER_CODE =
            "Customer code already exists";

    public static final String DUPLICATE_CUSTOMER_VAT_NUMBER =
            "Customer VAT number already exists";

    public static final String SUPPLIER_NOT_FOUND =
            "Supplier not found";

    public static final String SUPPLIER_INACTIVE =
            "Supplier is inactive";

    public static final String DUPLICATE_SUPPLIER_CODE =
            "Supplier code already exists";

    public static final String DUPLICATE_SUPPLIER_VAT_NUMBER =
            "Supplier VAT number already exists";

    public static final String CARRIER_NOT_FOUND =
            "Carrier not found";

    public static final String DUPLICATE_CARRIER_CODE =
            "Carrier code already exists";

    public static final String DUPLICATE_CARRIER_VAT_NUMBER =
            "Carrier VAT number already exists";

    public static final String DUPLICATE_CARRIER_LICENSE_NUMBER =
            "Carrier license number already exists";

    public static final String PRODUCT_CATEGORY_NOT_FOUND =
            "Product category not found";

    public static final String DUPLICATE_PRODUCT_CATEGORY_CODE =
            "Product category code already exists";

    public static final String DUPLICATE_PRODUCT_CATEGORY_NAME =
            "Product category name already exists";

    public static final String PRODUCT_NOT_FOUND =
            "Product not found";

    public static final String DUPLICATE_PRODUCT_SKU =
            "Product SKU already exists";

    public static final String PRODUCT_CATEGORY_INACTIVE =
            "Product category is inactive";

    public static final String INVENTORY_NOT_FOUND =
            "Inventory not found";

    public static final String DUPLICATE_INVENTORY_POSITION =
            "Inventory already exists for this product and warehouse";

    public static final String PRODUCT_INACTIVE =
            "Product is inactive";

    public static final String STOCK_MOVEMENT_NOT_FOUND =
            "Stock movement not found";

    public static final String DUPLICATE_STOCK_MOVEMENT_REFERENCE =
            "Stock movement reference already exists";

    public static final String ADJUSTMENT_REASON_REQUIRED =
            "Reason is required for stock adjustment movements";

    public static final String INSUFFICIENT_STOCK =
            "Insufficient stock for this movement";

    public static final String RESERVED_STOCK_EXCEEDS_AVAILABLE_STOCK =
            "Stock movement would reduce physical stock below reserved stock";

    public static final String AUTHENTICATED_USER_REQUIRED =
            "Authenticated user is required";

    // PURCHASE ORDER

    public static final String PURCHASE_ORDER_NOT_FOUND =
            "Purchase order not found";

    public static final String DUPLICATE_PURCHASE_ORDER_NUMBER =
            "Purchase order number already exists";

    public static final String PURCHASE_ORDER_MUST_BE_DRAFT =
            "Purchase order must be in DRAFT status";

    public static final String PURCHASE_ORDER_MUST_BE_SUBMITTED =
            "Only SUBMITTED purchase orders can be approved";

    public static final String PURCHASE_ORDER_WITHOUT_ITEMS =
            "Purchase order cannot be submitted without items";

    public static final String PURCHASE_ORDER_CANNOT_BE_CANCELLED =
            "Purchase order cannot be cancelled from its current status";

    public static final String PURCHASE_ORDER_HAS_RECEIVED_GOODS =
            "Purchase order cannot be cancelled because goods have already been received";

    public static final String EXPECTED_DELIVERY_DATE_INVALID =
            "Expected delivery date cannot be before order date";


    // PURCHASE ORDER ITEM

    public static final String PURCHASE_ORDER_ITEM_NOT_FOUND =
            "Purchase order item not found";

    public static final String DUPLICATE_PURCHASE_ORDER_PRODUCT =
            "Product already exists in purchase order";

    public static final String ORDERED_QUANTITY_INVALID =
            "Ordered quantity must be greater than zero";

    public static final String UNIT_PRICE_INVALID =
            "Unit price must be zero or greater";

    public static final String ORDERED_QUANTITY_BELOW_RECEIVED =
            "Ordered quantity cannot be lower than received quantity";

    public static final String RECEIVED_ITEM_CANNOT_BE_REMOVED =
            "Cannot remove an item that has already been received";

    // GOODS RECEPTION

    public static final String GOODS_RECEPTION_NOT_FOUND =
            "Goods reception not found";

    public static final String DUPLICATE_GOODS_RECEPTION_REFERENCE =
            "Goods reception reference already exists";

    public static final String GOODS_RECEPTION_REFERENCE_REQUIRED =
            "Goods reception reference is required";

    public static final String GOODS_RECEPTION_NOT_ALLOWED =
            "Goods reception is only allowed for APPROVED or PARTIALLY_RECEIVED purchase orders";

    public static final String GOODS_RECEPTION_QUANTITY_INVALID =
            "Goods reception quantity must be greater than zero";

    public static final String GOODS_RECEPTION_EXCEEDS_ORDERED_QUANTITY =
            "Received quantity cannot exceed ordered quantity";

    public static final String INVENTORY_POSITION_REQUIRED_FOR_RECEPTION =
            "Inventory position is required before receiving goods";

}
