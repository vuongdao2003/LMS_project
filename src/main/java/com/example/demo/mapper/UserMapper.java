package com.example.demo.mapper;

import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;

public class UserMapper {
    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();
    }
}
