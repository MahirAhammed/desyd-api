package com.desyd.api.repository;

import com.desyd.api.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    Optional<UserProfile> findByUsername(String username);
    boolean existsByUsername(String username); // TODO: username exists case insensitive and only differs by 2 characters

}
