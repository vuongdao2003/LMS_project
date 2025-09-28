package com.example.demo.service.User;

import com.example.demo.dto.request.ChangePasswordRequest;
import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdateRequest;
import com.example.demo.dto.response.UserResponse;

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
