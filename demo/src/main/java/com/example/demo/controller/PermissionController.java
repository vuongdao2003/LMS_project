package com.example.demo.controller;

import com.example.demo.dto.request.PermissionRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.PermissionResponse;
import com.example.demo.service.Permission.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionController {
    PermissionService permissionService;
    @PostMapping
    public ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest permission) {
        return  ApiResponse.<PermissionResponse>builder()
                .result(permissionService.createPermission(permission)).build();
    }
    @GetMapping
    public ApiResponse<List<PermissionResponse>> getPermission() {
        return ApiResponse.<List<PermissionResponse>>builder().result(permissionService.getPermissions()).build();
    }
    @DeleteMapping("/{name}")
    public ApiResponse<Void> deletePermission(@PathVariable String name) {
        permissionService.deletePermission(name);
        return ApiResponse.<Void>builder().build();
    }
}
