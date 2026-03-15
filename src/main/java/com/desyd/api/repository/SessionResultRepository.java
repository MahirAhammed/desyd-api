package com.desyd.api.repository;

import com.desyd.api.entity.SessionResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionResultRepository extends JpaRepository<SessionResult, UUID> {

    Optional<SessionResult> findBySessionId(UUID uuid);
}
