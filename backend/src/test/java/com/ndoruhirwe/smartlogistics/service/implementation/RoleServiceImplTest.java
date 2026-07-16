package com.ndoruhirwe.smartlogistics.service.implementation;

import com.ndoruhirwe.smartlogistics.dto.request.RoleRequest;
import com.ndoruhirwe.smartlogistics.dto.response.RoleResponse;
import com.ndoruhirwe.smartlogistics.entity.Role;
import com.ndoruhirwe.smartlogistics.exception.DuplicateResourceException;
import com.ndoruhirwe.smartlogistics.exception.ResourceNotFoundException;
import com.ndoruhirwe.smartlogistics.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private RoleServiceImpl roleService;

    private UUID roleId;
    private Role role;

    @BeforeEach
    void setUp() {
        roleService = new RoleServiceImpl(roleRepository);

        roleId = UUID.randomUUID();

        role = Role.builder()
                .id(roleId)
                .name("ADMIN")
                .description("System Administrator")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createRole_shouldSaveAndReturnRoleResponse() {
        // Arrange
        RoleRequest request = mock(RoleRequest.class);

        when(request.getName()).thenReturn("ADMIN");
        when(request.getDescription()).thenReturn("System Administrator");

        when(roleRepository.findByNameIgnoreCase("ADMIN"))
                .thenReturn(Optional.empty());

        when(roleRepository.save(any(Role.class)))
                .thenReturn(role);

        // Act
        RoleResponse response = roleService.createRole(request);

        // Assert
        assertNotNull(response);
        assertEquals(roleId, response.getId());
        assertEquals("ADMIN", response.getName());
        assertEquals("System Administrator", response.getDescription());

        verify(roleRepository).findByNameIgnoreCase("ADMIN");
        verify(roleRepository).save(any(Role.class));
    }

    @Test
    void createRole_shouldThrowDuplicateResourceException_whenNameAlreadyExists() {
        // Arrange
        RoleRequest request = mock(RoleRequest.class);

        when(request.getName()).thenReturn("ADMIN");

        when(roleRepository.findByNameIgnoreCase("ADMIN"))
                .thenReturn(Optional.of(role));

        // Act et Assert
        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> roleService.createRole(request)
        );

        assertEquals("Role already exists", exception.getMessage());

        verify(roleRepository).findByNameIgnoreCase("ADMIN");
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void getAllRoles_shouldReturnRoleResponseList() {
        // Arrange
        Role managerRole = Role.builder()
                .id(UUID.randomUUID())
                .name("MANAGER")
                .description("Warehouse Manager")
                .createdAt(LocalDateTime.now())
                .build();

        when(roleRepository.findAll())
                .thenReturn(List.of(role, managerRole));

        // Act
        List<RoleResponse> responses = roleService.getAllRoles();

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("ADMIN", responses.get(0).getName());
        assertEquals("MANAGER", responses.get(1).getName());

        verify(roleRepository).findAll();
    }

    @Test
    void getRoleById_shouldReturnRoleResponse_whenRoleExists() {
        // Arrange
        when(roleRepository.findById(roleId))
                .thenReturn(Optional.of(role));

        // Act
        RoleResponse response = roleService.getRoleById(roleId);

        // Assert
        assertNotNull(response);
        assertEquals(roleId, response.getId());
        assertEquals("ADMIN", response.getName());

        verify(roleRepository).findById(roleId);
    }

    @Test
    void getRoleById_shouldThrowResourceNotFoundException_whenRoleDoesNotExist() {
        // Arrange
        when(roleRepository.findById(roleId))
                .thenReturn(Optional.empty());

        // Act et Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> roleService.getRoleById(roleId)
        );

        assertEquals("Role not found", exception.getMessage());

        verify(roleRepository).findById(roleId);
    }

    @Test
    void updateRole_shouldUpdateAndReturnRoleResponse() {
        // Arrange
        RoleRequest request = mock(RoleRequest.class);

        when(request.getName()).thenReturn("CARGO_ADMIN");
        when(request.getDescription())
                .thenReturn("Cargo operations administrator");

        when(roleRepository.findById(roleId))
                .thenReturn(Optional.of(role));

        when(roleRepository.findByNameIgnoreCase("CARGO_ADMIN"))
                .thenReturn(Optional.empty());

        when(roleRepository.save(any(Role.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        RoleResponse response = roleService.updateRole(roleId, request);

        // Assert
        assertNotNull(response);
        assertEquals("CARGO_ADMIN", response.getName());
        assertEquals(
                "Cargo operations administrator",
                response.getDescription()
        );

        verify(roleRepository).findById(roleId);
        verify(roleRepository).findByNameIgnoreCase("CARGO_ADMIN");
        verify(roleRepository).save(role);
    }

    @Test
    void deleteRole_shouldDeleteRole_whenRoleExists() {
        // Arrange
        when(roleRepository.existsById(roleId))
                .thenReturn(true);

        // Act
        roleService.deleteRole(roleId);

        // Assert
        verify(roleRepository).existsById(roleId);
        verify(roleRepository).deleteById(roleId);
    }

    @Test
    void deleteRole_shouldThrowResourceNotFoundException_whenRoleDoesNotExist() {
        // Arrange
        when(roleRepository.existsById(roleId))
                .thenReturn(false);

        // Act et Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> roleService.deleteRole(roleId)
        );

        assertEquals("Role not found", exception.getMessage());

        verify(roleRepository).existsById(roleId);
        verify(roleRepository, never()).deleteById(any(UUID.class));
    }
}
