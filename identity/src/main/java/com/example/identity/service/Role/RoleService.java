package com.example.identity.service.Role;

import com.example.identity.dto.request.RoleRequest;
import com.example.identity.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse createRole(RoleRequest roleRequest);
    List<RoleResponse> getRoles();
    Void deleteRole(String name);
}
