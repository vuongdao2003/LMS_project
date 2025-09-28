package com.example.profile.mapper;

import com.example.profile.dto.request.SocialLinkRequest;
import com.example.profile.dto.response.SocialLinkResponse;
import com.example.profile.entity.SocialLink;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SocialLinkMapper {
    SocialLinkResponse toSocialLinkResponse(SocialLink socialLink) ;
    SocialLink toSocialLink(SocialLinkRequest socialLinkRequest);
}
