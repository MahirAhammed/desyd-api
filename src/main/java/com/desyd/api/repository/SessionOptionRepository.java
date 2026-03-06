package com.desyd.api.repository;

import com.desyd.api.entity.SessionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionOptionRepository extends JpaRepository<SessionOption, UUID> {

    List<SessionOption> findBySessionIdOrderByCreatedAt(UUID sessionId);
    Optional<SessionOption> findBySessionIdAndOptionText(UUID sessionId, String optionText);
    boolean existsBySessionIdAndOptionText(UUID sessionId, String optionText);
    void deleteBySessionId(UUID sessionId);
}
