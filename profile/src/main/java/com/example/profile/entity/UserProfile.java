package com.example.profile.entity;

import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Node("user_profile")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants(level = AccessLevel.PRIVATE)
@Builder
public class UserProfile {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;


    String userId;

    String firstName;
    String lastName;
    LocalDate birthDate;
    String city;
    String description;

    @Relationship(type = "HAS_LINK")
     List<SocialLink> socialLinks = new ArrayList<>();
}
