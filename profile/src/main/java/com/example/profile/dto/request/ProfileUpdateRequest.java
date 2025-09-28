package com.example.profile.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProfileUpdateRequest {
    String firstName;
    String lastName;
    LocalDate birthDate;
    String city;
    String description;
}
