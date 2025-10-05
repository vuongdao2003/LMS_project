package com.example.identity.service.Permission;

import com.example.identity.dto.request.PermissionRequest;
import com.example.identity.dto.response.PermissionResponse;
import com.example.identity.entity.Permission;
import com.example.identity.mapper.PermissionMapper;
import com.example.identity.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal=true)
public class PermissionServiceImpl implements PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Override
    public PermissionResponse createPermission(PermissionRequest permission) {
        Permission permissionEntity = permissionMapper.toPermission(permission);
        permissionEntity = permissionRepository.save(permissionEntity);
        return permissionMapper.toPermissionResponse(permissionEntity);
    }

    @Override
    public List<PermissionResponse> getPermissions() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    @Override
    public void deletePermission(String name) {
        permissionRepository.deleteById(name);
    }

}

