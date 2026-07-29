package com.ndoruhirwe.smartlogistics.controller;

import com.ndoruhirwe.smartlogistics.dto.request.RoleRequest;
import com.ndoruhirwe.smartlogistics.dto.response.RoleResponse;
import com.ndoruhirwe.smartlogistics.exception.ResourceNotFoundException;
import com.ndoruhirwe.smartlogistics.service.RoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoleController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RoleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoleService roleService;

    @Test
    void createRole_shouldReturn201Created() throws Exception {
        UUID roleId = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();

        RoleResponse response = new RoleResponse(
                roleId,
                "ADMIN",
                "System Administrator",
                createdAt,
                null
        );

        when(roleService.createRole(any(RoleRequest.class)))
                .thenReturn(response);

        String requestBody = """
                {
                  "name": "ADMIN",
                  "description": "System Administrator"
                }
                """;

        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON
                ))
                .andExpect(jsonPath("$.id").value(roleId.toString()))
                .andExpect(jsonPath("$.name").value("ADMIN"))
                .andExpect(jsonPath("$.description")
                        .value("System Administrator"))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").isEmpty());

        verify(roleService).createRole(any(RoleRequest.class));
    }

    @Test
    void getAllRoles_shouldReturn200AndRoleList() throws Exception {
        RoleResponse admin = new RoleResponse(
                UUID.randomUUID(),
                "ADMIN",
                "System Administrator",
                LocalDateTime.now(),
                null
        );

        RoleResponse manager = new RoleResponse(
                UUID.randomUUID(),
                "MANAGER",
                "Warehouse Manager",
                LocalDateTime.now(),
                null
        );

        when(roleService.getAllRoles())
                .thenReturn(List.of(admin, manager));

        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON
                ))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("ADMIN"))
                .andExpect(jsonPath("$[1].name").value("MANAGER"));

        verify(roleService).getAllRoles();
    }

    @Test
    void getRoleById_shouldReturn200_whenRoleExists() throws Exception {
        UUID roleId = UUID.randomUUID();

        RoleResponse response = new RoleResponse(
                roleId,
                "MANAGER",
                "Warehouse Manager",
                LocalDateTime.now(),
                null
        );

        when(roleService.getRoleById(roleId))
                .thenReturn(response);

        mockMvc.perform(get("/api/roles/{id}", roleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(roleId.toString()))
                .andExpect(jsonPath("$.name").value("MANAGER"))
                .andExpect(jsonPath("$.description")
                        .value("Warehouse Manager"));

        verify(roleService).getRoleById(roleId);
    }

    @Test
    void getRoleById_shouldReturn404_whenRoleDoesNotExist()
            throws Exception {

        UUID roleId = UUID.randomUUID();

        when(roleService.getRoleById(roleId))
                .thenThrow(
                        new ResourceNotFoundException("Role not found")
                );

        mockMvc.perform(get("/api/roles/{id}", roleId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON
                ))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Role not found"));

        verify(roleService).getRoleById(roleId);
    }

    @Test
    void updateRole_shouldReturn200AndUpdatedRole() throws Exception {
        UUID roleId = UUID.randomUUID();

        RoleResponse response = new RoleResponse(
                roleId,
                "CARGO_ADMIN",
                "Cargo operations administrator",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(roleService.updateRole(
                eq(roleId),
                any(RoleRequest.class)
        )).thenReturn(response);

        String requestBody = """
                {
                  "name": "CARGO_ADMIN",
                  "description": "Cargo operations administrator"
                }
                """;

        mockMvc.perform(put("/api/roles/{id}", roleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(roleId.toString()))
                .andExpect(jsonPath("$.name").value("CARGO_ADMIN"))
                .andExpect(jsonPath("$.description")
                        .value("Cargo operations administrator"))
                .andExpect(jsonPath("$.updatedAt").exists());

        verify(roleService).updateRole(
                eq(roleId),
                any(RoleRequest.class)
        );
    }

    @Test
    void deleteRole_shouldReturn204NoContent() throws Exception {
        UUID roleId = UUID.randomUUID();

        doNothing().when(roleService).deleteRole(roleId);

        mockMvc.perform(delete("/api/roles/{id}", roleId))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(roleService).deleteRole(roleId);
    }

    @Test
    void createRole_shouldReturn400_whenNameIsBlank()
            throws Exception {

        String requestBody = """
                {
                  "name": "",
                  "description": "Invalid role"
                }
                """;

        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(roleService, never())
                .createRole(any(RoleRequest.class));
    }


}
