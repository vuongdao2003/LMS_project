package com.example.profile.service;

import com.example.profile.dto.request.ProfileCreationRequest;
import com.example.profile.dto.request.SocialLinkRequest;
import com.example.profile.dto.response.SocialLinkResponse;
import com.example.profile.dto.response.UserProfileResponse;
import com.example.profile.entity.SocialLink;

import java.util.List;


public interface UserProfileService {
    UserProfileResponse createUserProfile(ProfileCreationRequest profileCreationRequest);
    UserProfileResponse getUserProfile(String id);
    boolean deleteUserProfile(String id);
    List<SocialLink> getSocialLinks(String userId);
    SocialLinkResponse addSocialLink(String userId, SocialLinkRequest request);
    SocialLinkResponse updateSocialLink(String userId, String linkId, SocialLinkRequest request);

}
