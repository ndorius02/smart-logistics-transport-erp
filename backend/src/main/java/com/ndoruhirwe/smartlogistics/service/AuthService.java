package com.ndoruhirwe.smartlogistics.service;

import com.ndoruhirwe.smartlogistics.dto.request.LoginRequest;
import com.ndoruhirwe.smartlogistics.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
}
