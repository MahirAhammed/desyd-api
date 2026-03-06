package com.desyd.api.dto.response;

import com.desyd.api.enums.SessionCategory;
import com.desyd.api.enums.SessionStatus;
import com.desyd.api.enums.VotingMode;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class SessionResponse {

    private UUID id;
    private String sessionCode;
    private String title;
    private String description;
    private SessionCategory category;
    private VotingMode voteMode;
    private SessionStatus status;
    private Integer maxParticipants;
    private Integer currentParticipantCount;
    private Boolean allowParticipantOptions;
    private Boolean anonymousVoting;
    private Boolean showLiveResults;
    private Integer duration;
    private String createdByUsername;
    private Boolean isCreator;
    private Boolean hasVoted;
    private OffsetDateTime createdAt;
    private OffsetDateTime closedAt;
    private OffsetDateTime expiresAt;
    private List<OptionResponse> options;

    public SessionResponse(){}

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

    public VotingMode getVoteMode() {
        return voteMode;
    }

    public void setVoteMode(VotingMode voteMode) {
        this.voteMode = voteMode;
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

    public Integer getCurrentParticipantCount() {
        return currentParticipantCount;
    }

    public void setCurrentParticipantCount(Integer currentParticipantCount) {
        this.currentParticipantCount = currentParticipantCount;
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

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }

    public Boolean getCreator() {
        return isCreator;
    }

    public void setCreator(Boolean creator) {
        isCreator = creator;
    }

    public Boolean getHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(Boolean hasVoted) {
        this.hasVoted = hasVoted;
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

    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(OffsetDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public List<OptionResponse> getOptions() {
        return options;
    }

    public void setOptions(List<OptionResponse> options) {
        this.options = options;
    }
}
