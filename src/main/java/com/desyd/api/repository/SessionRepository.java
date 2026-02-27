package com.desyd.api.repository;

import com.desyd.api.entity.Session;
import com.desyd.api.enums.SessionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {

    Optional<Session> findBySessionCode(String sessionCode);
    boolean existsBySessionCode(String sessionCode);
    List<Session> findByCreatedBy(UUID userId);
    Page<Session> findByCreatedBy(UUID userId, Pageable pageable);
    List<Session> findByCreatedByAndStatus(UUID userId, SessionStatus status);

    // TODO: Query to find expired sessions that are ACTIVE

}
