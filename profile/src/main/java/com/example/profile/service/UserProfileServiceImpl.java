package com.example.profile.service;

import com.example.profile.dto.request.ProfileCreationRequest;
import com.example.profile.dto.request.ProfileUpdateRequest;
import com.example.profile.dto.request.SocialLinkRequest;
import com.example.profile.dto.response.ProfileUpdateResponse;
import com.example.profile.dto.response.SocialLinkResponse;
import com.example.profile.dto.response.UserProfileResponse;
import com.example.profile.entity.SocialLink;
import com.example.profile.entity.UserProfile;
import com.example.profile.mapper.SocialLinkMapper;
import com.example.profile.mapper.UserProfileMapper;
import com.example.profile.repository.UserProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal=true)
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper  userProfileMapper;
    SocialLinkMapper socialLinkMapper;
    @Override
    public UserProfileResponse createUserProfile(ProfileCreationRequest profileCreationRequest) {
        UserProfile userProfile = userProfileMapper.toUserProfile(profileCreationRequest);
        log.info("Creating user profile {}", userProfile);
        userProfileRepository.save(userProfile);
        return userProfileMapper.toResponse(userProfile);
    }

    @Override
    public UserProfileResponse getUserProfile(String id) {
        UserProfile userProfile = userProfileRepository.findById(id).orElseThrow(() ->new  RuntimeException("Profile not found"));

        return userProfileMapper.toResponse(userProfile);
    }

    @Override
    public boolean deleteUserProfile(String id) {
        if(userProfileRepository.findById(id).isPresent()){
            userProfileRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<SocialLink> getSocialLinks(String userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId);
        List<SocialLink> socialLinks = userProfile.getSocialLinks();

        return socialLinks;
    }

    @Override
    public SocialLinkResponse addSocialLink(String userId, SocialLinkRequest request) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId);
        SocialLink socialLink = SocialLink.builder()
                .platform(request.getPlatform())
                .url(request.getUrl())
                .build();
        userProfile.getSocialLinks().add(socialLink);
        userProfileRepository.save(userProfile);
        return socialLinkMapper.toSocialLinkResponse(socialLink);
    }

    @Override
    public SocialLinkResponse updateSocialLink(String userId, String linkId, SocialLinkRequest request) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId);

        SocialLink link = userProfile.getSocialLinks().stream()
                .filter(l -> l.getId().equals(linkId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Social link not found"));

        link.setPlatform(request.getPlatform());
        link.setUrl(request.getUrl());

        userProfileRepository.save(userProfile);
        return socialLinkMapper.toSocialLinkResponse(link);
    }

    @Override
    public ProfileUpdateResponse updateProfile(String userId, ProfileUpdateRequest request) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId);
        userProfileMapper.updateProfile(request,userProfile);
        return userProfileMapper.toUserProfileResponse(userProfileRepository.save(userProfile));
    }


}
