package com.example.demo.configuaration;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
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
    @Bean
    ApplicationRunner initApplicationRunner(UserRepository userRepository) {
        return args -> {
            if(userRepository.findByUsername("ADMIN").isEmpty()){
              var roles = new HashSet<String>();
              roles.add("ADMIN");
              User user = User.builder()
                      .username("admin")
                      .password(passwordEncoder.encode("admin"))
                      .email("admin@gmail.com")
                      //.role(roles)
                      .build();
              userRepository.save(user);
              log.warn("admin user has been created with default password: admin");
            }
        };
    }
}
