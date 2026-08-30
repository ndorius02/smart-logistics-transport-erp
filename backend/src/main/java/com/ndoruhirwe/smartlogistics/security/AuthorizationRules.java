package com.ndoruhirwe.smartlogistics.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;

@Component

public class AuthorizationRules {
    public void configure(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> auth

                // =========================
                // AUTHENTICATION
                // =========================

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/auth/login"
                ).permitAll()


                // =========================
                // USERS
                // =========================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/users",
                        "/api/users/**"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER"
                )

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/users",
                        "/api/users/**"
                ).hasRole("ADMIN")

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/users/**"
                ).hasRole("ADMIN")

                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/users/**"
                ).hasRole("ADMIN")


                // =========================
                // ROLES
                // =========================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/roles",
                        "/api/roles/**"
                ).authenticated()

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/roles",
                        "/api/roles/**"
                ).hasRole("ADMIN")

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/roles/**"
                ).hasRole("ADMIN")

                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/roles/**"
                ).hasRole("ADMIN")

                // =========================
                // WAREHOUSES
                // =========================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/warehouses",
                        "/api/warehouses/**"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER",
                        "WAREHOUSE_OFFICER"
                )

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/warehouses",
                        "/api/warehouses/**"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER"
                )

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/warehouses/**"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER"
                )

                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/warehouses/*/activate",
                        "/api/warehouses/*/deactivate"
                ).hasRole("ADMIN")

                // =========================
                // VEHICLES
                // =========================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/vehicles",
                        "/api/vehicles/**"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER",
                        "TRANSPORT_COORDINATOR"
                )

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/vehicles"
                ).hasAnyRole(
                        "ADMIN",
                        "TRANSPORT_COORDINATOR"
                )

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/vehicles/**"
                ).hasAnyRole(
                        "ADMIN",
                        "TRANSPORT_COORDINATOR"
                )

                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/vehicles/*/activate",
                        "/api/vehicles/*/deactivate"
                ).hasAnyRole(
                        "ADMIN",
                        "TRANSPORT_COORDINATOR"
                )

                // =========================
                // DRIVERS
                // =========================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/drivers",
                        "/api/drivers/**"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER",
                        "TRANSPORT_COORDINATOR"
                )

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/drivers"
                ).hasAnyRole(
                        "ADMIN",
                        "TRANSPORT_COORDINATOR"
                )

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/drivers/**"
                ).hasAnyRole(
                        "ADMIN",
                        "TRANSPORT_COORDINATOR"
                )

                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/drivers/*/activate",
                        "/api/drivers/*/deactivate"
                ).hasAnyRole(
                        "ADMIN",
                        "TRANSPORT_COORDINATOR"
                )

                // =========================
                // TRANSPORTS
                // =========================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/transports",
                        "/api/transports/**"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER",
                        "TRANSPORT_COORDINATOR"
                )

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/transports"
                ).hasAnyRole(
                        "ADMIN",
                        "TRANSPORT_COORDINATOR"
                )


                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/transports/**"
                ).hasAnyRole(
                        "ADMIN",
                        "TRANSPORT_COORDINATOR"
                )


                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/transports/*/start",
                        "/api/transports/*/complete",
                        "/api/transports/*/cancel"
                ).hasAnyRole(
                        "ADMIN",
                        "TRANSPORT_COORDINATOR"
                )

                // =========================
                // CUSTOMERS
                // =========================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/customers",
                        "/api/customers/**"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER",
                        "WAREHOUSE_OFFICER",
                        "TRANSPORT_COORDINATOR"
                )

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/customers"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER"
                )

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/customers/**"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER"
                )

                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/customers/*/activate",
                        "/api/customers/*/deactivate"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER"
                )

                // =========================
                // SUPPLIERS
                // =========================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/suppliers",
                        "/api/suppliers/**"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER",
                        "WAREHOUSE_OFFICER",
                        "TRANSPORT_COORDINATOR"
                )

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/suppliers"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER"
                )

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/suppliers/**"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER"
                )

                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/suppliers/*/activate",
                        "/api/suppliers/*/deactivate"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER"
                )

                // =========================
                // CARRIERS
                // =========================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/carriers",
                        "/api/carriers/**"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER",
                        "WAREHOUSE_OFFICER",
                        "TRANSPORT_COORDINATOR"
                )

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/carriers"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER"
                )

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/carriers/**"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER"
                )

                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/carriers/*/activate",
                        "/api/carriers/*/deactivate"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER"
                )

                // =========================
                //  PRODUCT CATEGORIES
                // =========================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/product-categories",
                        "/api/product-categories/**"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER",
                        "WAREHOUSE_OFFICER",
                        "TRANSPORT_COORDINATOR"
                )

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/product-categories"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER"
                )

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/product-categories/**"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER"
                )

                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/product-categories/*/activate",
                        "/api/product-categories/*/deactivate"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER"
                )

                // =========================
                // PRODUCTS
                // =========================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/products",
                        "/api/products/**"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER",
                        "WAREHOUSE_OFFICER",
                        "TRANSPORT_COORDINATOR"
                )

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/products"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER"
                )

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/products/**"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER"
                )

                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/products/*/activate",
                        "/api/products/*/deactivate"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER"
                )

                // =========================
                // INVENTORY
                // =========================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/inventory",
                        "/api/inventory/**"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER",
                        "WAREHOUSE_OFFICER",
                        "TRANSPORT_COORDINATOR"
                )

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/inventory"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER",
                        "WAREHOUSE_OFFICER"
                )

                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/inventory/*/minimum-stock-level"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER",
                        "WAREHOUSE_OFFICER"
                )

                // =========================
                // STOCK MOVEMENTS
                // =========================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/stock-movements",
                        "/api/stock-movements/**"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER",
                        "WAREHOUSE_OFFICER",
                        "TRANSPORT_COORDINATOR"
                )

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/stock-movements"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER",
                        "WAREHOUSE_OFFICER"
                )

                // =========================
                // PURCHASE ORDERS
                // =========================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/purchase-orders",
                        "/api/purchase-orders/**"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER",
                        "WAREHOUSE_OFFICER",
                        "TRANSPORT_COORDINATOR"
                )

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/purchase-orders",
                        "/api/purchase-orders/*/items"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER"
                )

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/purchase-orders/**"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER"
                )

                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/purchase-orders/*/items/*"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER"
                )

                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/purchase-orders/*/submit",
                        "/api/purchase-orders/*/approve",
                        "/api/purchase-orders/*/cancel"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER"
                )

                // =========================
                // GOODS RECEPTIONS
                // =========================


                .requestMatchers(
                        HttpMethod.GET,
                        "/api/goods-receptions",
                        "/api/goods-receptions/**"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER",
                        "WAREHOUSE_OFFICER",
                        "TRANSPORT_COORDINATOR"
                )

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/goods-receptions"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER",
                        "WAREHOUSE_OFFICER"
                )









                // =========================
                // DEFAULT RULE
                // =========================

                .anyRequest()
                .authenticated()
        );
    }
}
