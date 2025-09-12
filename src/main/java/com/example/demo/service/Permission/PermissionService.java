package com.example.demo.service.Permission;

import com.example.demo.dto.request.PermissionRequest;
import com.example.demo.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {
    PermissionResponse createPermission(PermissionRequest permission);
    List<PermissionResponse> getPermissions();
    void deletePermission(String name);
}
