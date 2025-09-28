package com.example.profile.entity;

import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

@Node("social")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants(level = AccessLevel.PRIVATE)
@Builder
public class SocialLink {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;


    String platform;
    String url;
}
