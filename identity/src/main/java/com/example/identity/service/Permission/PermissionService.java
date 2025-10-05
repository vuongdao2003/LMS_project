package com.example.identity.service.Permission;

import com.example.identity.dto.request.PermissionRequest;
import com.example.identity.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {
    PermissionResponse createPermission(PermissionRequest permission);
    List<PermissionResponse> getPermissions();
    void deletePermission(String name);
}
