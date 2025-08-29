package com.example.demo.controller;

import com.example.demo.dto.response.UserResponse;
import com.example.demo.service.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @GetMapping("/me")
    public UserResponse getMyInfo() {
        return userService.getMyInfo();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }
}
