package com.example.profile.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProfileUpdateResponse {
    String firstName;
    String lastName;
    LocalDate birthDate;
    String city;
    String description;
}
