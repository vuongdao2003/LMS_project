package com.example.demo.repository.httpclient;

import com.example.demo.dto.request.ProfileCreationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile",url = "http://localhost:8888/profile")
public interface ProfileClient {
    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
     Object creationProfile(@RequestBody ProfileCreationRequest profileCreationRequest);

}
