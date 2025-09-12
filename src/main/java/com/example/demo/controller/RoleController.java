package com.example.demo.controller;

import com.example.demo.dto.request.PermissionRequest;
import com.example.demo.dto.request.RoleRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.PermissionResponse;
import com.example.demo.dto.response.RoleResponse;
import com.example.demo.service.Permission.PermissionService;
import com.example.demo.service.Role.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    RoleService roleService;
    @PostMapping
    public ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest roleRequest) {
        return  ApiResponse.<RoleResponse>builder()
                .result(roleService.createRole(roleRequest)).build();
    }
    @GetMapping
    public ApiResponse<List<RoleResponse>> getRole() {
        return ApiResponse.<List<RoleResponse>>builder().result(roleService.getRoles()).build();
    }
    @DeleteMapping("/{name}")
    public ApiResponse<Void> deletePermission(@PathVariable String name) {
        roleService.deleteRole(name);
        return ApiResponse.<Void>builder().build();
    }
}
