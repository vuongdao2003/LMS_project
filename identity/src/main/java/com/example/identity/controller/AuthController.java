package com.example.identity.controller;

import com.example.identity.dto.request.AuthenticationRequest;
import com.example.identity.dto.request.IntrospectRequest;
import com.example.identity.dto.request.LogoutRequest;
import com.example.identity.dto.response.ApiResponse;
import com.example.identity.dto.response.AuthenticationResponse;
import com.example.identity.dto.response.IntrospectResponse;
import com.example.identity.repository.UserRepository;
import com.example.identity.service.Auth.AuthService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    UserRepository userRepository;
    AuthService authService;
    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest){
        var result = authService.authenticate(authenticationRequest);

        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }
    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect( @RequestBody IntrospectRequest introspectRequest) throws ParseException, JOSEException {
        var result = authService.introspect(introspectRequest);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest logoutRequest) throws ParseException, JOSEException {
         authService.logout(logoutRequest);
        return ApiResponse.<Void>builder()
                .build();
    }
}
