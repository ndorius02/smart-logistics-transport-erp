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
}
