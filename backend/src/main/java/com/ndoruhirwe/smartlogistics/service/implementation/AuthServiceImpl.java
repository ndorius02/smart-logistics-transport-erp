package com.ndoruhirwe.smartlogistics.service.implementation;

import com.ndoruhirwe.smartlogistics.dto.request.LoginRequest;
import com.ndoruhirwe.smartlogistics.dto.response.AuthResponse;
import com.ndoruhirwe.smartlogistics.service.AuthService;
import com.ndoruhirwe.smartlogistics.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.email(),
                                request.password()
                        )
                );

        String token = jwtService.generateToken(authentication);

        List<String> authorities = authentication.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .toList();

        return new AuthResponse(
                token,
                "Bearer",
                authentication.getName(),
                authorities
        );
    }

}
