package com.desyd.api.dto.response;

import java.util.UUID;

public class UserProfileResponse {

    private UUID id;
    private String username;
    private String email;
    private Integer sessionsJoined;
    private Integer sessionsHosted;

    public UserProfileResponse(){}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
