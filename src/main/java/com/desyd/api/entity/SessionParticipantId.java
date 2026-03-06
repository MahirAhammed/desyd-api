package com.desyd.api.entity;

import jakarta.persistence.Column;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class SessionParticipantId implements Serializable {
    @Column(name = "session_id")
    private UUID sessionId;
    @Column(name = "user_id")
    private UUID userId;

    public SessionParticipantId(){}

    public SessionParticipantId(UUID session, UUID user) {
        this.sessionId = session;
        this.userId = user;
    }

    public UUID getSession() {
        return sessionId;
    }

    public void setSession(UUID session) {
        this.sessionId = session;
    }

    public UUID getUser() {
        return userId;
    }

    public void setUser(UUID user) {
        this.userId = user;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SessionParticipantId that = (SessionParticipantId) o;
        return Objects.equals(sessionId, that.sessionId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, userId);
    }
}
