package com.desyd.api.dto.response;

import java.util.List;

public class VoteResponse {

    private Integer totalVotesSubmitted;
    private Boolean hasVeto;
    private List<VoteDetail> votes;

    public static record VoteDetail(String optionText, Integer value){}

    public VoteResponse(){}

    public VoteResponse(Integer totalVotesSubmitted, Boolean hasVeto, List<VoteDetail> votes) {
        this.totalVotesSubmitted = totalVotesSubmitted;
        this.hasVeto = hasVeto;
        this.votes = votes;
    }

    public Integer getTotalVotesSubmitted() {
        return totalVotesSubmitted;
    }

    public void setTotalVotesSubmitted(Integer totalVotesSubmitted) {
        this.totalVotesSubmitted = totalVotesSubmitted;
    }

    public Boolean getHasVeto() {
        return hasVeto;
    }

    public void setHasVeto(Boolean hasVeto) {
        this.hasVeto = hasVeto;
    }

    public List<VoteDetail> getVotes() {
        return votes;
    }

    public void setVotes(List<VoteDetail> votes) {
        this.votes = votes;
    }
}
