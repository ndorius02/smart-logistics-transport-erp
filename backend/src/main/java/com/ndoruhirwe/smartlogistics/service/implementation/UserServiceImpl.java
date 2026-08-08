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
import com.ndoruhirwe.smartlogistics.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import static com.ndoruhirwe.smartlogistics.exception.ErrorMessages.DUPLICATE_USER_EMAIL;
import static com.ndoruhirwe.smartlogistics.exception.ErrorMessages.USER_NOT_FOUND;
import static com.ndoruhirwe.smartlogistics.exception.ErrorMessages.ROLE_NOT_FOUND;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

@Service
@Transactional

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse createUser(UserCreateRequest request) {

        String normalizedEmail = request.email()
                .trim()
                .toLowerCase();

        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new DuplicateResourceException(DUPLICATE_USER_EMAIL);
        }

        Role role = roleRepository.findById(request.roleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(ROLE_NOT_FOUND)
                );

        User user = userMapper.toEntity(request);
        user.setEmail(normalizedEmail);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(request.password()));
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {
        User user = findUserById(id);
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse updateUser(UUID id, UserUpdateRequest request ) {
        User user = findUserById(id);
        String normalizedEmail = request.email()
                .trim()
                .toLowerCase();

        userRepository.findByEmailIgnoreCase(normalizedEmail)
                .filter(existingUser -> !existingUser.getId().equals(id))
                .ifPresent(existingUser -> {
                    throw new DuplicateResourceException(DUPLICATE_USER_EMAIL);
                });

        Role role = roleRepository.findById(request.roleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(ROLE_NOT_FOUND)
                );

        userMapper.updateEntity(request, user);

        user.setEmail(normalizedEmail);
        user.setRole(role);

        user.setUpdatedAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }

    @Override
    public void deleteUser(UUID id) {

        User user = findUserById(id);

        userRepository.delete(user);
    }

    private User findUserById(UUID id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(USER_NOT_FOUND)
                );
    }

}
