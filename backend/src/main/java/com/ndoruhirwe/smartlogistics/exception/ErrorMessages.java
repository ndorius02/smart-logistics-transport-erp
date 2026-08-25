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

}
