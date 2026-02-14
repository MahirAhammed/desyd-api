package com.desyd.api.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    private UUID userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(unique = true, nullable = false, length = 30)
    private String username;

    @Column(name = "sessions_joined", nullable = false)
    private Integer sessionsJoined;

    @Column(name = "sessions_hosted", nullable = false)
    private Integer sessionsHosted;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public UserProfile() {}

    public UserProfile(User user, String username) {
        this.user = user;
        this.username = username;
        this.sessionsHosted = 0;
        this.sessionsJoined = 0;
    }

    @PrePersist
    protected void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getSessionsJoined() {
        return sessionsJoined;
    }

    public void setSessionsJoined(Integer sessionsJoined) {
        this.sessionsJoined = sessionsJoined;
    }

    public Integer getSessionsHosted() {
        return sessionsHosted;
    }

    public void setSessionsHosted(Integer sessionsHosted) {
        this.sessionsHosted = sessionsHosted;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(user, that.user) && Objects.equals(username, that.username) && Objects.equals(sessionsJoined, that.sessionsJoined) && Objects.equals(sessionsHosted, that.sessionsHosted) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, username, sessionsJoined, sessionsHosted, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "user=" + user +
                ", username='" + username + '\'' +
                ", sessionsJoined=" + sessionsJoined +
                ", sessionsHosted=" + sessionsHosted +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
