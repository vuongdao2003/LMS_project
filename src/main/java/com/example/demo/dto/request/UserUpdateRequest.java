package com.example.demo.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserUpdateRequest {
    String firstName;
    String lastName;
    String password;
    String email;
    Set<String> roles;
}
