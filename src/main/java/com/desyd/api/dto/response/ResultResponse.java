package com.desyd.api.dto.response;

import com.desyd.api.enums.SessionStatus;
import com.desyd.api.enums.VotingMode;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class ResultResponse {

    private UUID sessionId;
    private String sessionCode;
    private String title;
    private VotingMode voteMode;
    private Integer totalVotes;
    private Integer totalParticipants;
    private UUID winnerOptionId;
    private String winnerOption;
    private Integer winnerScore;
    private OffsetDateTime sessionClosedAt;
    private List<OptionResult> results;

    public static class OptionResult{
        private UUID optionId;
        private String option;
        private Integer value;
        private Integer rank;
        private Integer voteCount;
        private Integer vetoCount;

        public OptionResult(){}

        public UUID getOptionId() {
            return optionId;
        }

        public void setOptionId(UUID optionId) {
            this.optionId = optionId;
        }

        public String getOption() {
            return option;
        }

        public void setOption(String option) {
            this.option = option;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public Integer getRank() {
            return rank;
        }

        public void setRank(Integer rank) {
            this.rank = rank;
        }

        public Integer getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(Integer voteCount) {
            this.voteCount = voteCount;
        }

        public Integer getVetoCount() {
            return vetoCount;
        }

        public void setVetoCount(Integer vetoCount) {
            this.vetoCount = vetoCount;
        }
    }

    public ResultResponse(){}

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public VotingMode getVoteMode() {
        return voteMode;
    }

    public void setVoteMode(VotingMode voteMode) {
        this.voteMode = voteMode;
    }

    public UUID getWinnerOptionId() { return winnerOptionId; }

    public void setWinnerOptionId(UUID winnerOptionId) { this.winnerOptionId = winnerOptionId; }

    public String getWinnerOption() {
        return winnerOption;
    }

    public void setWinnerOption(String winnerOption) {
        this.winnerOption = winnerOption;
    }

    public Integer getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(Integer totalVotes) {
        this.totalVotes = totalVotes;
    }

    public Integer getTotalParticipants() { return totalParticipants; }

    public void setTotalParticipants(Integer totalParticipants) { this.totalParticipants = totalParticipants;}

    public List<OptionResult> getResults() { return results; }

    public void setResults(List<OptionResult> results) {
        this.results = results;
    }

    public String getSessionCode() { return sessionCode;    }

    public void setSessionCode(String sessionCode) { this.sessionCode = sessionCode;}

    public Integer getWinnerScore() { return winnerScore; }

    public void setWinnerScore(Integer winnerScore) { this.winnerScore = winnerScore; }

    public OffsetDateTime getSessionClosedAt() { return sessionClosedAt; }

    public void setSessionClosedAt(OffsetDateTime sessionClosedAt) { this.sessionClosedAt = sessionClosedAt; }
}
