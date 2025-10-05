package com.example.identity.repository.httpclient;

import com.example.identity.dto.request.ProfileCreationRequest;
import com.example.identity.dto.response.ProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile",url = "${app.service.profile}")
public interface ProfileClient {
    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    ProfileResponse creationProfile(@RequestBody ProfileCreationRequest profileCreationRequest);

}
