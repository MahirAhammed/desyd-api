package com.desyd.api.entity;

import com.desyd.api.enums.SessionCategory;
import com.desyd.api.enums.SessionStatus;
import com.desyd.api.enums.VotingMode;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.*;

@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "session_code", nullable = false, unique = true, length = 6)
    private String sessionCode;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, columnDefinition = "session_category")
    private SessionCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "voting_mode", nullable = false, columnDefinition = "vote_mode")
    private VotingMode votingMode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "session_status")
    private SessionStatus status = SessionStatus.ACTIVE;

    @Column(name = "max_participants", nullable = false)
    private Integer maxParticipants = 10;

    @Column(name = "allow_participant_options", nullable = false)
    private Boolean allowParticipantOptions = true;

    @Column(name = "anonymous_voting", nullable = false)
    private Boolean anonymousVoting = false;

    @Column(name = "show_live_results", nullable = false)
    private Boolean showLiveResults = true;

    @Column(name = "duration")
    private Integer duration = 600;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "closed_at")
    private OffsetDateTime closedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User createdBy;

    // One-to-many relationship with SessionOption
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessionOption> options = new ArrayList<>();

    // One-to-many relationship with SessionParticipant
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SessionParticipant> participants = new HashSet<>();

    // One-to-many relationship with Vote
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votes = new ArrayList<>();

    // One-to-one relationship with SessionResult
    @OneToOne(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private SessionResult result;

    public Session() {}

    public Session(String sessionCode, String title, String description, SessionCategory category, VotingMode votingMode, Integer maxParticipants, Integer duration, Boolean allowParticipantOptions, Boolean anonymousVoting, Boolean showLiveResults, User createdBy) {
        this.sessionCode = sessionCode;
        this.title = title;
        this.description = description;
        this.category = category;
        this.votingMode = votingMode;
        this.maxParticipants = maxParticipants;
        this.duration = duration;
        this.allowParticipantOptions = allowParticipantOptions;
        this.anonymousVoting = anonymousVoting;
        this.showLiveResults = showLiveResults;
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

    public String getSessionCode() {
        return sessionCode;
    }

    public void setSessionCode(String sessionCode) {
        this.sessionCode = sessionCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SessionCategory getCategory() {
        return category;
    }

    public void setCategory(SessionCategory category) {
        this.category = category;
    }

    public VotingMode getVotingMode() {
        return votingMode;
    }

    public void setVotingMode(VotingMode votingMode) {
        this.votingMode = votingMode;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public Boolean getAllowParticipantOptions() {
        return allowParticipantOptions;
    }

    public void setAllowParticipantOptions(Boolean allowParticipantOptions) {
        this.allowParticipantOptions = allowParticipantOptions;
    }

    public Boolean getAnonymousVoting() {
        return anonymousVoting;
    }

    public void setAnonymousVoting(Boolean anonymousVoting) {
        this.anonymousVoting = anonymousVoting;
    }

    public Boolean getShowLiveResults() {
        return showLiveResults;
    }

    public void setShowLiveResults(Boolean showLiveResults) {
        this.showLiveResults = showLiveResults;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(OffsetDateTime closedAt) {
        this.closedAt = closedAt;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public List<SessionOption> getOptions() {
        return options;
    }

    public void setOptions(List<SessionOption> options) {
        this.options = options;
    }

    public Set<SessionParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<SessionParticipant> participants) {
        this.participants = participants;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public SessionResult getResult() {
        return result;
    }

    public void setResult(SessionResult result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return Objects.equals(id, session.id) && Objects.equals(sessionCode, session.sessionCode) && Objects.equals(title, session.title) && Objects.equals(description, session.description) && category == session.category && votingMode == session.votingMode && status == session.status && Objects.equals(maxParticipants, session.maxParticipants) && Objects.equals(allowParticipantOptions, session.allowParticipantOptions) && Objects.equals(anonymousVoting, session.anonymousVoting) && Objects.equals(showLiveResults, session.showLiveResults) && Objects.equals(duration, session.duration) && Objects.equals(createdAt, session.createdAt) && Objects.equals(closedAt, session.closedAt) && Objects.equals(createdBy, session.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sessionCode, title, description, category, votingMode, status, maxParticipants, allowParticipantOptions, anonymousVoting, showLiveResults, duration, createdAt, closedAt, createdBy);
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", sessionCode='" + sessionCode + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", votingMode=" + votingMode +
                ", status=" + status +
                ", maxParticipants=" + maxParticipants +
                ", allowParticipantOptions=" + allowParticipantOptions +
                ", anonymousVoting=" + anonymousVoting +
                ", showLiveResults=" + showLiveResults +
                ", duration=" + duration +
                ", createdAt=" + createdAt +
                ", closedAt=" + closedAt +
                ", createdBy=" + createdBy +
                '}';
    }
}
