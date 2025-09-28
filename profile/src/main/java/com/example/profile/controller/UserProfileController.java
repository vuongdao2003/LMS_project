package com.example.profile.controller;

import com.example.profile.dto.request.ProfileCreationRequest;
import com.example.profile.dto.request.ProfileUpdateRequest;
import com.example.profile.dto.request.SocialLinkRequest;
import com.example.profile.dto.response.ProfileUpdateResponse;
import com.example.profile.dto.response.SocialLinkResponse;
import com.example.profile.dto.response.UserProfileResponse;
import com.example.profile.entity.UserProfile;
import com.example.profile.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping()
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal=true)

public class UserProfileController {
    UserProfileService userProfileService;
    @PostMapping("/users")
    public UserProfileResponse creationProfile(@RequestBody ProfileCreationRequest profileCreationRequest) throws  Exception{
        return userProfileService.createUserProfile(profileCreationRequest);
    }
    @GetMapping("/{id}")
    public UserProfileResponse getUserProfile(@PathVariable String id){

        return userProfileService.getUserProfile(id);
    }

    @PostMapping("/{userId}/social-links")
    public ResponseEntity<SocialLinkResponse> addSocialLink(
            @PathVariable String userId,
            @RequestBody SocialLinkRequest request) {
        SocialLinkResponse response = userProfileService.addSocialLink(userId, request);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/update/{userId}")
    public ProfileUpdateResponse updateUserProfile(@RequestBody ProfileUpdateRequest profileUpdateRequest, @PathVariable String userId){
        return userProfileService.updateProfile(userId,profileUpdateRequest);
    }

    @PutMapping("/{userId}/social-links/{linkId}")
    public ResponseEntity<SocialLinkResponse> updateSocialLink(
            @PathVariable String userId,
            @PathVariable String linkId,
            @RequestBody SocialLinkRequest request) {
        SocialLinkResponse response = userProfileService.updateSocialLink(userId, linkId, request);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/social-link/{userId}")
    public List<?> getSocialLink(@PathVariable String userId){
        return userProfileService.getSocialLinks(userId);
    }
}
