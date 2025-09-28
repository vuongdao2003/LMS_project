package com.example.profile.dto.response;

import com.example.profile.entity.SocialLink;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SocialLinkResponse {
    private String id;
    private String platform;
    private String url;
}
