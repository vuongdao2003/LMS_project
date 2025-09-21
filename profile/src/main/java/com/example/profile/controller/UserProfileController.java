package com.example.profile.controller;

import com.example.profile.dto.request.ProfileCreationRequest;
import com.example.profile.dto.response.UserProfileResponse;
import com.example.profile.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
@RequestMapping()
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal=true)

public class UserProfileController {
    UserProfileService userProfileService;
    @PostMapping("users")
    public UserProfileResponse creationProfile(@RequestBody ProfileCreationRequest profileCreationRequest) throws  Exception{
        return userProfileService.createUserProfile(profileCreationRequest);
    }
    @GetMapping("/{id}")
    public UserProfileResponse getUserProfile(@PathVariable String id){
        return userProfileService.getUserProfile(id);
    }
}
