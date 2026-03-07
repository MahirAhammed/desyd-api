package com.desyd.api.entity;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class VoteInput {

    @NotNull(message = "No option provided")
    private UUID optionId;

    @NotNull(message = "Vote value is required")
    private Integer value;

    public VoteInput(){}

    public UUID getOptionId() {
        return optionId;
    }

    public void setOptionId(UUID optionId) {
        this.optionId = optionId;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
