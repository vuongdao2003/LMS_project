package com.example.identity.mapper;

import com.example.identity.dto.request.ProfileCreationRequest;
import com.example.identity.dto.request.UserCreationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileCreationRequest toProfileCreationRequest(UserCreationRequest reg);
}
