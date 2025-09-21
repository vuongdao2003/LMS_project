package com.example.demo.service.Role;

import com.example.demo.dto.request.RoleRequest;
import com.example.demo.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse createRole(RoleRequest roleRequest);
    List<RoleResponse> getRoles();
    Void deleteRole(String name);
}
