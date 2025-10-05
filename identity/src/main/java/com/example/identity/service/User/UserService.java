package com.example.identity.service.User;

import com.example.identity.dto.request.ChangePasswordRequest;
import com.example.identity.dto.request.UserCreationRequest;
import com.example.identity.dto.request.UserUpdateRequest;
import com.example.identity.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse getUserById(String id);
    UserResponse getMyInfo();
    void deleteUser(String id);
    UserResponse CreateUser(UserCreationRequest reg);
    UserResponse UpdateUser(String id,UserUpdateRequest reg);
    String changePassword(String username, ChangePasswordRequest reg);
}
