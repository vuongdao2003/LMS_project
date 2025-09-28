package com.example.demo.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProfileResponse {
    String firstName;
    String lastName;
    LocalDate birthDate;
    String city;
    String description;
}
