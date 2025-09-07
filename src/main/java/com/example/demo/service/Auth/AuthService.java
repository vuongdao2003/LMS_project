package com.example.demo.service.Auth;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse regiter(RegisterRequest reg);
    AuthResponse login(LoginRequest login);
}
