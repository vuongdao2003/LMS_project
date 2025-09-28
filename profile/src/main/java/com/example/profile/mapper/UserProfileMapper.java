package com.example.profile.mapper;

import com.example.profile.dto.request.ProfileCreationRequest;
import com.example.profile.dto.response.UserProfileResponse;
import com.example.profile.entity.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfileResponse toResponse(UserProfile userProfile);
    UserProfile toUserProfile(ProfileCreationRequest profileCreationRequest);
}
