package com.example.profile.service;

import com.example.profile.dto.request.ProfileCreationRequest;
import com.example.profile.dto.response.UserProfileResponse;


public interface UserProfileService {
    UserProfileResponse createUserProfile(ProfileCreationRequest profileCreationRequest);
    UserProfileResponse getUserProfile(String id);
}
