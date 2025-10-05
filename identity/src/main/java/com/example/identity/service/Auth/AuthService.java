package com.example.identity.service.Auth;

import com.example.identity.dto.request.*;
import com.example.identity.dto.response.AuthenticationResponse;
import com.example.identity.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthService {
   AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
   IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
   void logout(LogoutRequest request) throws ParseException, JOSEException;
}
