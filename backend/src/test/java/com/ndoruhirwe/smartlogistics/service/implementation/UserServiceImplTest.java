package com.ndoruhirwe.smartlogistics.service.implementation;

import com.ndoruhirwe.smartlogistics.dto.request.UserCreateRequest;
import com.ndoruhirwe.smartlogistics.dto.request.UserUpdateRequest;
import com.ndoruhirwe.smartlogistics.dto.response.UserResponse;
import com.ndoruhirwe.smartlogistics.entity.Role;
import com.ndoruhirwe.smartlogistics.entity.User;
import com.ndoruhirwe.smartlogistics.exception.DuplicateResourceException;
import com.ndoruhirwe.smartlogistics.exception.ResourceNotFoundException;
import com.ndoruhirwe.smartlogistics.mapper.UserMapper;
import com.ndoruhirwe.smartlogistics.repository.RoleRepository;
import com.ndoruhirwe.smartlogistics.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserServiceImpl userService;

    private UUID userId;
    private UUID roleId;
    private Role role;
    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(
                userRepository,
                roleRepository,
                userMapper,
                passwordEncoder
        );

        userId = UUID.randomUUID();
        roleId = UUID.randomUUID();

        role = Role.builder()
                .id(roleId)
                .name("MANAGER")
                .description("Warehouse manager")
                .createdAt(LocalDateTime.now())
                .build();

        user = User.builder()
                .id(userId)
                .firstName("Alice")
                .lastName("Martin")
                .email("alice.martin@example.com")
                .password("$2a$10$hashedPassword")
                .phoneNumber("+32470000123")
                .active(true)
                .role(role)
                .createdAt(LocalDateTime.now())
                .build();

        userResponse = new UserResponse(
                userId,
                "Alice",
                "Martin",
                "alice.martin@example.com",
                "+32470000123",
                true,
                roleId,
                "MANAGER",
                user.getCreatedAt(),
                null
        );
    }

    @Test
    void createUser_shouldSaveUserAndReturnResponse() {
        // Arrange
        UserCreateRequest request = new UserCreateRequest(
                "Alice",
                "Martin",
                " Alice.Martin@Example.com ",
                "Password123!",
                "+32470000123",
                roleId
        );

        User mappedUser = User.builder()
                .firstName("Alice")
                .lastName("Martin")
                .phoneNumber("+32470000123")
                .active(true)
                .build();

        when(userRepository.existsByEmailIgnoreCase(
                "alice.martin@example.com"
        )).thenReturn(false);

        when(roleRepository.findById(roleId))
                .thenReturn(Optional.of(role));

        when(userMapper.toEntity(request))
                .thenReturn(mappedUser);

        when(passwordEncoder.encode("Password123!"))
                .thenReturn("$2a$10$hashedPassword");

        when(userRepository.save(mappedUser))
                .thenReturn(user);

        when(userMapper.toResponse(user))
                .thenReturn(userResponse);

        // Act
        UserResponse response = userService.createUser(request);

        // Assert
        assertNotNull(response);
        assertEquals(userId, response.id());
        assertEquals("Alice", response.firstName());
        assertEquals("alice.martin@example.com", response.email());
        assertEquals("MANAGER", response.roleName());

        assertEquals(
                "alice.martin@example.com",
                mappedUser.getEmail()
        );
        assertEquals(
                "$2a$10$hashedPassword",
                mappedUser.getPassword()
        );
        assertEquals(role, mappedUser.getRole());

        verify(userRepository)
                .existsByEmailIgnoreCase("alice.martin@example.com");

        verify(roleRepository).findById(roleId);
        verify(userMapper).toEntity(request);
        verify(passwordEncoder).encode("Password123!");
        verify(userRepository).save(mappedUser);
        verify(userMapper).toResponse(user);
    }

    @Test
    void createUser_shouldThrowDuplicateResourceException_whenEmailExists() {
        // Arrange
        UserCreateRequest request = new UserCreateRequest(
                "Alice",
                "Martin",
                "alice.martin@example.com",
                "Password123!",
                "+32470000123",
                roleId
        );

        when(userRepository.existsByEmailIgnoreCase(
                "alice.martin@example.com"
        )).thenReturn(true);

        // Act et Assert
        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> userService.createUser(request)
        );

        assertEquals(
                "A user with this email already exists",
                exception.getMessage()
        );

        verify(userRepository)
                .existsByEmailIgnoreCase("alice.martin@example.com");

        verify(roleRepository, never()).findById(any(UUID.class));
        verify(userMapper, never()).toEntity(any());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_shouldThrowResourceNotFoundException_whenRoleDoesNotExist() {
        // Arrange
        UserCreateRequest request = new UserCreateRequest(
                "Alice",
                "Martin",
                "alice.martin@example.com",
                "Password123!",
                "+32470000123",
                roleId
        );

        when(userRepository.existsByEmailIgnoreCase(
                "alice.martin@example.com"
        )).thenReturn(false);

        when(roleRepository.findById(roleId))
                .thenReturn(Optional.empty());

        // Act et Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.createUser(request)
        );

        assertEquals("Role not found", exception.getMessage());

        verify(roleRepository).findById(roleId);
        verify(userMapper, never()).toEntity(any());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getAllUsers_shouldReturnUserResponseList() {
        // Arrange
        User secondUser = User.builder()
                .id(UUID.randomUUID())
                .firstName("Jean")
                .lastName("Ndoruhirwe")
                .email("jean@example.com")
                .password("$2a$10$anotherHash")
                .active(true)
                .role(role)
                .createdAt(LocalDateTime.now())
                .build();

        UserResponse secondResponse = new UserResponse(
                secondUser.getId(),
                "Jean",
                "Ndoruhirwe",
                "jean@example.com",
                null,
                true,
                roleId,
                "MANAGER",
                secondUser.getCreatedAt(),
                null
        );

        when(userRepository.findAll())
                .thenReturn(List.of(user, secondUser));

        when(userMapper.toResponse(user))
                .thenReturn(userResponse);

        when(userMapper.toResponse(secondUser))
                .thenReturn(secondResponse);

        // Act
        List<UserResponse> responses = userService.getAllUsers();

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Alice", responses.get(0).firstName());
        assertEquals("Jean", responses.get(1).firstName());

        verify(userRepository).findAll();
        verify(userMapper).toResponse(user);
        verify(userMapper).toResponse(secondUser);
    }

    @Test
    void getUserById_shouldReturnUserResponse_whenUserExists() {
        // Arrange
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(userMapper.toResponse(user))
                .thenReturn(userResponse);

        // Act
        UserResponse response = userService.getUserById(userId);

        // Assert
        assertNotNull(response);
        assertEquals(userId, response.id());
        assertEquals("Alice", response.firstName());

        verify(userRepository).findById(userId);
        verify(userMapper).toResponse(user);
    }

    @Test
    void getUserById_shouldThrowResourceNotFoundException_whenUserDoesNotExist() {
        // Arrange
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Act et Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserById(userId)
        );

        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findById(userId);
        verify(userMapper, never()).toResponse(any(User.class));
    }

    @Test
    void updateUser_shouldUpdateUserAndReturnResponse() {
        // Arrange
        UserUpdateRequest request = new UserUpdateRequest(
                "Alicia",
                "Martin",
                " Alicia.Martin@Example.com ",
                "+32471111222",
                true,
                roleId
        );

        UserResponse updatedResponse = new UserResponse(
                userId,
                "Alicia",
                "Martin",
                "alicia.martin@example.com",
                "+32471111222",
                true,
                roleId,
                "MANAGER",
                user.getCreatedAt(),
                LocalDateTime.now()
        );

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(userRepository.findByEmailIgnoreCase(
                "alicia.martin@example.com"
        )).thenReturn(Optional.empty());

        when(roleRepository.findById(roleId))
                .thenReturn(Optional.of(role));

        when(userRepository.save(user))
                .thenReturn(user);

        when(userMapper.toResponse(user))
                .thenReturn(updatedResponse);

        // Act
        UserResponse response = userService.updateUser(
                userId,
                request
        );

        // Assert
        assertNotNull(response);
        assertEquals("Alicia", response.firstName());
        assertEquals(
                "alicia.martin@example.com",
                response.email()
        );

        assertEquals(
                "alicia.martin@example.com",
                user.getEmail()
        );
        assertEquals(role, user.getRole());
        assertNotNull(user.getUpdatedAt());

        verify(userRepository).findById(userId);

        verify(userRepository)
                .findByEmailIgnoreCase("alicia.martin@example.com");

        verify(roleRepository).findById(roleId);
        verify(userMapper).updateEntity(request, user);
        verify(userRepository).save(user);
        verify(userMapper).toResponse(user);
    }

    @Test
    void updateUser_shouldThrowDuplicateResourceException_whenEmailBelongsToAnotherUser() {
        // Arrange
        UUID anotherUserId = UUID.randomUUID();

        User anotherUser = User.builder()
                .id(anotherUserId)
                .email("existing@example.com")
                .build();

        UserUpdateRequest request = new UserUpdateRequest(
                "Alice",
                "Martin",
                "existing@example.com",
                "+32470000123",
                true,
                roleId
        );

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(userRepository.findByEmailIgnoreCase(
                "existing@example.com"
        )).thenReturn(Optional.of(anotherUser));

        // Act et Assert
        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> userService.updateUser(userId, request)
        );

        assertEquals(
                "A user with this email already exists",
                exception.getMessage()
        );

        verify(userRepository).findById(userId);

        verify(userRepository)
                .findByEmailIgnoreCase("existing@example.com");

        verify(roleRepository, never()).findById(any(UUID.class));
        verify(userMapper, never()).updateEntity(any(), any());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_shouldAllowKeepingSameEmail() {
        // Arrange
        UserUpdateRequest request = new UserUpdateRequest(
                "Alice",
                "Martin",
                "alice.martin@example.com",
                "+32470000123",
                true,
                roleId
        );

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(userRepository.findByEmailIgnoreCase(
                "alice.martin@example.com"
        )).thenReturn(Optional.of(user));

        when(roleRepository.findById(roleId))
                .thenReturn(Optional.of(role));

        when(userRepository.save(user))
                .thenReturn(user);

        when(userMapper.toResponse(user))
                .thenReturn(userResponse);

        // Act
        UserResponse response = userService.updateUser(
                userId,
                request
        );

        // Assert
        assertNotNull(response);

        verify(userMapper).updateEntity(request, user);
        verify(userRepository).save(user);
    }

    @Test
    void updateUser_shouldThrowResourceNotFoundException_whenRoleDoesNotExist() {
        // Arrange
        UserUpdateRequest request = new UserUpdateRequest(
                "Alice",
                "Martin",
                "alice.martin@example.com",
                "+32470000123",
                true,
                roleId
        );

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(userRepository.findByEmailIgnoreCase(
                "alice.martin@example.com"
        )).thenReturn(Optional.of(user));

        when(roleRepository.findById(roleId))
                .thenReturn(Optional.empty());

        // Act et Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.updateUser(userId, request)
        );

        assertEquals("Role not found", exception.getMessage());

        verify(roleRepository).findById(roleId);
        verify(userMapper, never()).updateEntity(any(), any());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_shouldDeleteUser_whenUserExists() {
        // Arrange
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository).findById(userId);
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_shouldThrowResourceNotFoundException_whenUserDoesNotExist() {
        // Arrange
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Act et Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.deleteUser(userId)
        );

        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findById(userId);
        verify(userRepository, never()).delete(any(User.class));
    }

}
