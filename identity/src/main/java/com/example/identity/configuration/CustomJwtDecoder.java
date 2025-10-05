package com.example.identity.configuration;

import com.example.identity.dto.request.IntrospectRequest;
import com.example.identity.service.Auth.AuthService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.signKey}")
    private String signerKey;
    @Autowired
    private AuthService authService;
    private NimbusJwtDecoder jwtDecoder=null;
    @Override
    public Jwt decode(String token) throws JwtException {
        try{
            var response = authService.introspect(
                    IntrospectRequest.builder().token(token).build()
            );
            if(!response.isValid()){
                throw new JwtException("Invalid token");
            }
        }catch(JOSEException | ParseException e){
            throw new JwtException(e.getMessage());

        }
        if(Objects.isNull(jwtDecoder)){
            SecretKeySpec key = new SecretKeySpec(signerKey.getBytes(), "HmacSHA512");
            jwtDecoder = NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS512).build();
        }
        return jwtDecoder.decode(token);
    }
}
