package com.desyd.api.dto.request;

import com.desyd.api.entity.VoteInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public class SubmitVoteRequest {

    @NotEmpty(message = "No vote casted")
    @Valid
    private List<VoteInput> votes;

    @Valid
    private UUID vetoOptionId;  // Only for VETO mode

    public SubmitVoteRequest(){}

    public List<VoteInput> getVotes() {
        return votes;
    }

    public void setVotes(List<VoteInput> votes) {
        this.votes = votes;
    }

    public UUID getVetoOptionId() {
        return vetoOptionId;
    }

    public void setVetoOptionId(UUID vetoOptionId) {
        this.vetoOptionId = vetoOptionId;
    }
}
