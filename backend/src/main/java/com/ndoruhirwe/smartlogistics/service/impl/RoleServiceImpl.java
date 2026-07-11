package com.ndoruhirwe.smartlogistics.service.impl;

import com.ndoruhirwe.smartlogistics.dto.request.RoleRequest;
import com.ndoruhirwe.smartlogistics.dto.response.RoleResponse;
import com.ndoruhirwe.smartlogistics.entity.Role;
import com.ndoruhirwe.smartlogistics.repository.RoleRepository;
import com.ndoruhirwe.smartlogistics.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleResponse createRole(RoleRequest request) {
        Role role = Role.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        Role savedRole = roleRepository.save(role);
        return mapToResponse(savedRole);
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public RoleResponse getRoleById(UUID id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        return mapToResponse(role);
    }

    @Override
    public RoleResponse updateRole(UUID id, RoleRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        role.setName(request.getName());
        role.setDescription(request.getDescription());

        Role updatedRole = roleRepository.save(role);
        return mapToResponse(updatedRole);
    }

    @Override
    public void deleteRole(UUID id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Role not found");
        }
        roleRepository.deleteById(id);
    }

    private RoleResponse mapToResponse(Role role) {
        return new RoleResponse(
                role.getId(),
                role.getName(),
                role.getDescription(),
                role.getCreatedAt(),
                role.getUpdatedAt()
        );
    }

}
