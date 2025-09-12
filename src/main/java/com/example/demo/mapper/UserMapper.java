package com.example.demo.mapper;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdateRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel ="spring",uses = {RoleMapper.class})
public interface UserMapper {
    User toUser(UserCreationRequest userCreationRequest);
    UserResponse toUserResponse(User user);

    @Mapping(target = "roles" , ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);
}
