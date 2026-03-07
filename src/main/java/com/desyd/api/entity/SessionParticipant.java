package com.desyd.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "session_participants")
public class SessionParticipant {

    @EmbeddedId
    private SessionParticipantId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("sessionId")
    @JoinColumn(name = "session_id", nullable = false)
    @JsonIgnore
    private Session session;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "has_voted", nullable = false)
    private Boolean hasVoted = false;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private OffsetDateTime joinedAt;

    public SessionParticipant() {}

    public SessionParticipant(Session session, User user) {
        this.session = session;
        this.user = user;
        this.hasVoted = false;
    }

    @PrePersist
    protected void onCreate() {
        this.joinedAt = OffsetDateTime.now();
    }

    public SessionParticipantId getId() {return id;}

    public void setId(SessionParticipantId id) {this.id = id;}

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(Boolean hasVoted) {
        this.hasVoted = hasVoted;
    }

    public OffsetDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(OffsetDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SessionParticipant that = (SessionParticipant) o;
        return Objects.equals(session.getId(), that.session.getId()) && Objects.equals(user.getId(), that.user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(session.getId(), user.getId());
    }

    @Override
    public String toString() {
        return "SessionParticipant{" +
                "session=" + session +
                ", user=" + user +
                ", hasVoted=" + hasVoted +
                ", joinedAt=" + joinedAt +
                '}';
    }
}
