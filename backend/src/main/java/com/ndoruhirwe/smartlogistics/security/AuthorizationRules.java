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
                // DEFAULT RULE
                // =========================

                .anyRequest()
                .authenticated()
        );
    }
}
