package com.example.profile.repository;

import com.example.profile.entity.SocialLink;
import com.example.profile.entity.UserProfile;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProfileRepository extends Neo4jRepository<UserProfile, String> {
    List<SocialLink> findSocialLinksByUserId(String userId);
    UserProfile findByUserId(String userId);
}
