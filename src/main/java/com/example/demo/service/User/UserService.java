package com.example.demo.service.User;

import com.example.demo.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse getUserById(String id);
    UserResponse getMyInfo();
    void deleteUser(String id);
}
