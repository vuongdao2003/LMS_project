package com.example.profile.mapper;

import com.example.profile.dto.request.ProfileCreationRequest;
import com.example.profile.dto.response.UserProfileResponse;
import com.example.profile.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    @Mapping(source = "id", target = "id") // cái này MapStruct sẽ tự làm, có thể bỏ
    UserProfileResponse toResponse(UserProfile userProfile);

    @Mapping(target = "userid", ignore = true) // vì request không có userid
    UserProfile toUserProfile(ProfileCreationRequest profileCreationRequest);
}
