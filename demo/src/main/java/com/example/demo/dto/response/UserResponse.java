package com.example.demo.dto.response;

import com.example.demo.dto.request.ProfileCreationRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String email;
    String firstName;
    String lastName;
    String city;
    LocalDate birthDate;
    Set<RoleResponse> roles;

}
