package com.desyd.api.repository;

import com.desyd.api.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VoteRepository extends JpaRepository<Vote, UUID> {

    List<Vote> findBySessionIdAndUserId(UUID sessionId, UUID userId);
    void deleteBySessionIdAndUserId(UUID sessionId, UUID userId);
}
