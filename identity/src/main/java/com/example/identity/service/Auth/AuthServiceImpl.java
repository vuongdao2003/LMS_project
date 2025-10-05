    package com.example.identity.service.Auth;
    import com.example.identity.dto.request.AuthenticationRequest;
    import com.example.identity.dto.request.IntrospectRequest;
    import com.example.identity.dto.request.LogoutRequest;
    import com.example.identity.dto.response.AuthenticationResponse;
    import com.example.identity.dto.response.IntrospectResponse;
    import com.example.identity.entity.InvalidatedToken;
    import com.example.identity.entity.User;
    import com.example.identity.exception.AppException;
    import com.example.identity.exception.ErrorCode;
    import com.example.identity.repository.InvalidatedRepository;
    import com.example.identity.repository.UserRepository;
    import com.nimbusds.jose.*;
    import com.nimbusds.jose.crypto.MACSigner;
    import com.nimbusds.jose.crypto.MACVerifier;
    import com.nimbusds.jwt.JWTClaimsSet;
    import com.nimbusds.jwt.SignedJWT;
    import lombok.AccessLevel;
    import lombok.RequiredArgsConstructor;
    import lombok.experimental.FieldDefaults;
    import lombok.experimental.NonFinal;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;
    import org.springframework.util.CollectionUtils;

    import java.text.ParseException;
    import java.time.Instant;
    import java.time.temporal.ChronoUnit;
    import java.util.Date;
    import java.util.StringJoiner;
    import java.util.UUID;

    @Slf4j
    @Service
    @RequiredArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
    public class AuthServiceImpl implements  AuthService {
        UserRepository userRepository;
        InvalidatedRepository  invalidatedRepository;
        @NonFinal
        @Value("${jwt.signKey}")
        protected String SIGNER_KEY;

        @Override
        public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
            var user =  userRepository.findByUsername(authenticationRequest.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());
            if (!authenticated) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
            var token = generateToken(user);

            return AuthenticationResponse.builder()
                    .token(token).authenticated(true).build();
        }

        @Override
        public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
            var token = request.getToken();
            boolean isvalid =true;
            try{
                verifyToken(token);
            }catch (AppException e){
                isvalid=false;
            }


            return IntrospectResponse.builder().valid(isvalid).build();
        }

        @Override
        public void logout(LogoutRequest request) throws ParseException, JOSEException {
            var signToken = verifyToken(request.getToken());
            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();
            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jit)
                    .expiryTime(expiryTime)
                    .build();
            invalidatedRepository.save(invalidatedToken);
        }

        private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

            SignedJWT signedJWT = SignedJWT.parse(token);

            Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            var verified=signedJWT.verify(verifier);
            if(!(verified && expityTime.after(new Date()))){
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
            if(invalidatedRepository.existsById(signedJWT.getJWTClaimsSet()
                    .getJWTID()))
                throw new AppException(ErrorCode.UNAUTHORIZED);


            return signedJWT;
        }

        String generateToken(User user){
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
            JWTClaimsSet jwtClaimSet = new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .issuer("identity.com")
                    .issueTime(new Date())
                    .expirationTime(new Date(
                            Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                    ))
                    .jwtID(UUID.randomUUID().toString())
                    .claim("scope",buildScope(user))
                    .build();

            Payload payload = new Payload(jwtClaimSet.toJSONObject());
            JWSObject jwsObject = new JWSObject(header,payload);
            try {
                jwsObject.sign(new MACSigner(SIGNER_KEY));
                return jwsObject.serialize();
            } catch (JOSEException e) {
                throw new RuntimeException(e);
            }
        }
        private String buildScope(User user){
            StringJoiner scope = new StringJoiner(" ");
            if(!CollectionUtils.isEmpty(user.getRoles()))
                user.getRoles().forEach(role -> {
                    scope.add("ROLE_"+role.getName());
                    if(!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> scope.add(permission.getName()));
                });
            return scope.toString();
        }
    }
