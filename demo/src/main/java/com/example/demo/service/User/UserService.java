package com.example.demo.service.User;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdateRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse getUserById(String id);
    UserResponse getMyInfo();
    void deleteUser(String id);
    User CreateUser(UserCreationRequest reg);
    UserResponse UpdateUser(String id,UserUpdateRequest reg);
}
