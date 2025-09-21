package com.example.demo.service.Auth;

import com.example.demo.dto.request.*;
import com.example.demo.dto.response.AuthResponse;
import com.example.demo.dto.response.AuthenticationResponse;
import com.example.demo.dto.response.IntrospectResponse;
import com.example.demo.entity.User;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthService {
   AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
   IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
   void logout(LogoutRequest request) throws ParseException, JOSEException;
}
