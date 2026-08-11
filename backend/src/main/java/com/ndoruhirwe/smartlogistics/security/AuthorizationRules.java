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
                // Temporary rules
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
                        "/api/vehicles",
                        "/api/vehicles/**"
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
                        HttpMethod.DELETE,
                        "/api/vehicles/**"
                ).hasRole("ADMIN")


                // =========================
                // DRIVERS
                // Temporary rules
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
                        "/api/drivers",
                        "/api/drivers/**"
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
                        HttpMethod.DELETE,
                        "/api/drivers/**"
                ).hasRole("ADMIN")


                // =========================
                // TRANSPORTS
                // Temporary rules
                // =========================

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/transports",
                        "/api/transports/**"
                ).hasAnyRole(
                        "ADMIN",
                        "MANAGER",
                        "TRANSPORT_COORDINATOR",
                        "BACK_OFFICE_USER"
                )

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/transports",
                        "/api/transports/**"
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
                        HttpMethod.DELETE,
                        "/api/transports/**"
                ).hasRole("ADMIN")


                // =========================
                // DEFAULT RULE
                // =========================

                .anyRequest()
                .authenticated()
        );
    }
}
