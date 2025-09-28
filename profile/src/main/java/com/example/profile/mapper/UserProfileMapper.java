package com.example.profile.mapper;

import com.example.profile.dto.request.ProfileCreationRequest;
import com.example.profile.dto.request.ProfileUpdateRequest;
import com.example.profile.dto.response.ProfileUpdateResponse;
import com.example.profile.dto.response.UserProfileResponse;
import com.example.profile.entity.UserProfile;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserProfileMapper {
    UserProfileResponse toResponse(UserProfile userProfile);
    UserProfile toUserProfile(ProfileCreationRequest profileCreationRequest);


    @Mapping(target = "userId" , ignore = true)
    @Mapping(target = "socialLinks", ignore = true)
    void updateProfile(ProfileUpdateRequest profileUpdateRequest, @MappingTarget UserProfile userProfile);

    ProfileUpdateResponse toUserProfileResponse(UserProfile userProfile);
}
