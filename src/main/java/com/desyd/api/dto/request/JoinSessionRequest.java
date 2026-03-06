package com.desyd.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class JoinSessionRequest {

    @NotBlank(message = "Session code is required")
    @Pattern(regexp = "^[A-Z0-9]{6}$", message = "Session code must be 6 uppercase alphanumeric characters")
    private String sessionCode;

    public JoinSessionRequest() {}

    public JoinSessionRequest(String sessionCode) {
        this.sessionCode = sessionCode;
    }

    public String getSessionCode() {
        return sessionCode;
    }

    public void setSessionCode(String sessionCode) {
        this.sessionCode = sessionCode;
    }
}
