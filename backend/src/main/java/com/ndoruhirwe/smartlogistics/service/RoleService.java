package com.ndoruhirwe.smartlogistics.service;

import com.ndoruhirwe.smartlogistics.dto.request.RoleRequest;
import com.ndoruhirwe.smartlogistics.dto.response.RoleResponse;

import java.util.List;
import java.util.UUID;

public interface RoleService {
    RoleResponse createRole(RoleRequest request);

    List<RoleResponse> getAllRoles();

    RoleResponse getRoleById(UUID id);

    RoleResponse updateRole(UUID id, RoleRequest request);

    void deleteRole(UUID id);
}
