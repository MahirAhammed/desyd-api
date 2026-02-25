package com.desyd.api.dto.request;

import com.desyd.api.enums.SessionCategory;
import com.desyd.api.enums.VotingMode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.*;

public class CreateSessionRequest {

    @NotBlank(message = "Session title is required")
    @Size(min = 1, max = 60, message = "Title length is invalid")
    private String title;

    @Size(max = 255, message = "Description too long")
    private String description;

    @NotNull(message = "Category is required")
    private SessionCategory category;

    @NotNull(message = "Voting mode is required")
    private VotingMode votingMode;

    @Min(value = 2, message = "Minimum 2 participants required")
    @Max(value = 30, message = "Maximum participants cannot exceed 30")
    private Integer maxParticipants = 10;

    private Boolean allowParticipantOptions = true;
    private Boolean anonymousVoting = false;
    private Boolean showLiveResults = true;

    @Min(value = 5, message = "Time limit must be at least 5 minutes")
    @Max(value = 1440, message = "Time limit cannot exceed 24 hours")
    private Integer duration;

    @Valid
    @Size(min = 2, max = 20, message = "Must provide between 2 and 20 options")
    private List<String> options = new ArrayList<>();

    public CreateSessionRequest(){}

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

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
