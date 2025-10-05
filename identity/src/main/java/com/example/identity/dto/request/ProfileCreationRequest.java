package com.example.identity.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProfileCreationRequest {
    String userId;
    String firstName;
    String lastName;
    LocalDate birthDate;
    String city;
    String description;
}