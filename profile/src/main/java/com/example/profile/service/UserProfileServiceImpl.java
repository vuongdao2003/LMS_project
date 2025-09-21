package com.example.profile.service;

import com.example.profile.dto.request.ProfileCreationRequest;
import com.example.profile.dto.response.UserProfileResponse;
import com.example.profile.entity.UserProfile;
import com.example.profile.mapper.UserProfileMapper;
import com.example.profile.repository.UserProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal=true)
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper  userProfileMapper;

    @Override
    public UserProfileResponse createUserProfile(ProfileCreationRequest profileCreationRequest) {
        UserProfile userProfile = userProfileMapper.toUserProfile(profileCreationRequest);
        userProfileRepository.save(userProfile);
        return userProfileMapper.toResponse(userProfile);
    }

    @Override
    public UserProfileResponse getUserProfile(String id) {
        UserProfile userProfile = userProfileRepository.findById(id).orElseThrow(() ->new  RuntimeException("Profile not found"));

        return userProfileMapper.toResponse(userProfile);
    }
}
