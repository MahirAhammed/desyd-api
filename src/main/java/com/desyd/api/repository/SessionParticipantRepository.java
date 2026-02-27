package com.desyd.api.repository;

import com.desyd.api.entity.SessionParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionParticipantRepository extends JpaRepository<SessionParticipant, UUID> {

    boolean existsBySessionIdAndUserId(UUID sessionId, UUID userId);
    long countBySessionId(UUID sessionId);
    Optional<SessionParticipant> findBySessionIdAndUserId(UUID sessionId, UUID userId);
    List<SessionParticipant> findByUserId(UUID userId);
}
