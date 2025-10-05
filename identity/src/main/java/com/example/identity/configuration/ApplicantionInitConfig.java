package com.example.identity.configuration;

import com.example.identity.constant.PredefinedRole;
import com.example.identity.entity.Role;
import com.example.identity.entity.User;
import com.example.identity.repository.RoleRepository;
import com.example.identity.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicantionInitConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;

    RoleRepository roleRepository;
    @Bean
    ApplicationRunner initApplicationRunner(UserRepository userRepository) {
        return args -> {
            if(userRepository.findByUsername("ADMIN").isEmpty()){
                HashSet<Role> roles = new HashSet<>();
                roleRepository.findById(PredefinedRole.ADMIN_ROLE).ifPresent(roles::add);
              User user = User.builder()
                      .username("admin")
                      .password(passwordEncoder.encode("admin"))
                      .email("admin@gmail.com")
                      .roles(roles)
                      .build();
              userRepository.save(user);
              log.warn("admin user has been created with default password: admin");
            }
        };
    }
}
