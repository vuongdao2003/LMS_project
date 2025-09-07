    package com.example.demo.service.Auth;

    import com.example.demo.dto.request.LoginRequest;
    import com.example.demo.dto.request.RegisterRequest;
    import com.example.demo.dto.response.AuthResponse;
    import com.example.demo.entity.User;
    import com.example.demo.exception.DuplicateResourceException;
    import com.example.demo.mapper.UserMapper;
    import com.example.demo.repository.Specification.UserRepository;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;

    @Service
    @RequiredArgsConstructor
    public class AuthServiceImpl implements  AuthService {
        private final UserRepository userRepository;
        private final UserMapper userMapper;
        @Override
        public AuthResponse regiter(RegisterRequest reg) {
            // Check tồn tại
            if (userRepository.existsByUsername(reg.getUsername())) {
                throw new DuplicateResourceException("Username already exists");
            }
            if (userRepository.existsByEmail(reg.getEmail())) {
                throw new DuplicateResourceException("Email already exists");
            }
            User user = User.builder().username(reg.getUsername()).email(reg.getEmail())
                    .password(reg.getPassword())
                    .build();
            userRepository.save(user);
            return AuthResponse.builder().build();
        }

        @Override
        public AuthResponse login(LoginRequest login) {
            return null;
        }
    }
