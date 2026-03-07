package com.desyd.api.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "votes")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "vote_value", nullable = false)
    private Integer voteValue;

    @Column(name = "is_veto", nullable = false)
    private Boolean isVeto = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private SessionOption option;

    public Vote() {}

    // Constructor for DEFAULT, RANKED, POINTS voting mode
    public Vote(Session session, User user, SessionOption option, Integer voteValue) {
        this.session = session;
        this.user = user;
        this.option = option;
        this.voteValue = voteValue;
        this.isVeto = false;
    }

    // Constructor for VETO voting mode
    public Vote(Session session, User user, SessionOption option, Integer voteValue, Boolean isVeto) {
        this.session = session;
        this.user = user;
        this.option = option;
        this.voteValue = voteValue;
        this.isVeto = isVeto;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getVoteValue() {
        return voteValue;
    }

    public void setVoteValue(Integer voteValue) {
        this.voteValue = voteValue;
    }

    public Boolean getVeto() {
        return isVeto;
    }

    public void setVeto(Boolean veto) {
        isVeto = veto;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

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

    public SessionOption getOption() {
        return option;
    }

    public void setOption(SessionOption option) {
        this.option = option;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return Objects.equals(id, vote.id) && Objects.equals(voteValue, vote.voteValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, voteValue);
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", voteValue=" + voteValue +
                ", isVeto=" + isVeto +
                ", createdAt=" + createdAt +
                ", session=" + session +
                ", user=" + user +
                ", option=" + option +
                '}';
    }
}
