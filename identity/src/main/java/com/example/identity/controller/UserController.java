package com.example.identity.controller;

import com.example.identity.dto.request.ChangePasswordRequest;
import com.example.identity.dto.request.UserCreationRequest;
import com.example.identity.dto.request.UserUpdateRequest;
import com.example.identity.dto.response.ApiResponse;
import com.example.identity.dto.response.UserResponse;
import com.example.identity.service.User.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    private final RestClient.Builder builder;

    @PostMapping("/register")
    public ApiResponse<UserResponse>register(@RequestBody @Valid UserCreationRequest reg) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.CreateUser(reg)).build();
    }

    @GetMapping("/all")
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.<List<UserResponse>> builder()
                .result(userService.getAllUsers()).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        return  ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder().result(userService.getMyInfo()).build();
    }
    @PutMapping("/update/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String id ,@RequestBody UserUpdateRequest updateRequest) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.UpdateUser(id,updateRequest))
                .build();
    }
    @PutMapping("/chage-password")
    ApiResponse<String> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest, Authentication authentication) {
        String username = authentication.getName();
        return ApiResponse.<String>
        builder().result(userService.changePassword(username,changePasswordRequest)).build();
    }
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }
}
