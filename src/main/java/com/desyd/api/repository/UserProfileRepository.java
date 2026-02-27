package com.desyd.api.repository;

import com.desyd.api.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    Optional<UserProfile> findByUsername(String username);
    boolean existsByUsername(String username); // TODO: username exists case insensitive and only differs by 2 characters

    @Modifying
    @Query("UPDATE UserProfile SET sessionsHosted = sessionsHosted + 1 WHERE userId = :id")
    void incrementSessionsHosted(@Param("id") UUID id);

    @Modifying
    @Query("UPDATE UserProfile SET sessionsJoined = sessionsJoined + 1 WHERE userId = :id")
    void incrementSessionsJoined(@Param("id") UUID id);

}
