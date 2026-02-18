package com.desyd.api.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "session_results")
public class SessionResult {

    @Id
    private UUID sessionId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "session_id")
    private Session session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_option_id")
    private SessionOption winnerOption;

    @Column(name = "winner_option_text", nullable = false)
    private String winnerOptionText;

    @Column(name = "winner_score", nullable = false)
    private Integer winnerScore;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "full_results", nullable = false, columnDefinition = "jsonb")
    private Map<String, Object> fullResults;

    @Column(name = "total_votes_cast", nullable = false)
    private Integer totalVotesCast;

    @Column(name = "total_participants", nullable = false)
    private Integer totalParticipants;

    public SessionResult() {}

    public SessionResult(Session session, SessionOption winnerOption, String winnerOptionText, Integer winnerScore, Map<String, Object> fullResults, Integer totalVotesCast, Integer totalParticipants) {
        this.session = session;
        this.winnerOption = winnerOption;
        this.winnerOptionText = winnerOptionText;
        this.winnerScore = winnerScore;
        this.fullResults = fullResults;
        this.totalVotesCast = totalVotesCast;
        this.totalParticipants = totalParticipants;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public SessionOption getWinnerOption() {
        return winnerOption;
    }

    public void setWinnerOption(SessionOption winnerOption) {
        this.winnerOption = winnerOption;
    }

    public String getWinnerOptionText() {
        return winnerOptionText;
    }

    public void setWinnerOptionText(String winnerOptionText) {
        this.winnerOptionText = winnerOptionText;
    }

    public Integer getWinnerScore() {
        return winnerScore;
    }

    public void setWinnerScore(Integer winnerScore) {
        this.winnerScore = winnerScore;
    }

    public Map<String, Object> getFullResults() {
        return fullResults;
    }

    public void setFullResults(Map<String, Object> fullResults) {
        this.fullResults = fullResults;
    }

    public Integer getTotalVotesCast() {
        return totalVotesCast;
    }

    public void setTotalVotesCast(Integer totalVotesCast) {
        this.totalVotesCast = totalVotesCast;
    }

    public Integer getTotalParticipants() {
        return totalParticipants;
    }

    public void setTotalParticipants(Integer totalParticipants) {
        this.totalParticipants = totalParticipants;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SessionResult that = (SessionResult) o;
        return Objects.equals(sessionId, that.sessionId) && Objects.equals(session, that.session) && Objects.equals(winnerOption, that.winnerOption) && Objects.equals(winnerOptionText, that.winnerOptionText) && Objects.equals(winnerScore, that.winnerScore) && Objects.equals(fullResults, that.fullResults) && Objects.equals(totalVotesCast, that.totalVotesCast) && Objects.equals(totalParticipants, that.totalParticipants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, session, winnerOption, winnerOptionText, winnerScore, fullResults, totalVotesCast, totalParticipants);
    }

    @Override
    public String toString() {
        return "SessionResult{" +
                "sessionId=" + sessionId +
                ", session=" + session +
                ", winnerOption=" + winnerOption +
                ", winnerOptionText='" + winnerOptionText + '\'' +
                ", winnerScore=" + winnerScore +
                ", fullResults=" + fullResults +
                ", totalVotesCast=" + totalVotesCast +
                ", totalParticipants=" + totalParticipants +
                '}';
    }
}
