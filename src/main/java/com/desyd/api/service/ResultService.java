package com.desyd.api.service;

import com.desyd.api.dto.response.ResultResponse;
import com.desyd.api.entity.Session;
import com.desyd.api.entity.SessionOption;
import com.desyd.api.entity.SessionResult;
import com.desyd.api.entity.Vote;
import com.desyd.api.exception.ResourceNotFoundException;
import com.desyd.api.exception.ValidationException;
import com.desyd.api.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ResultService {

    private static final Logger logger = LoggerFactory.getLogger(ResultService.class);

    private final SessionRepository sessionRepository;
    private final SessionOptionRepository sessionOptionRepository;
    private final VoteRepository voteRepository;
    private final SessionResultRepository sessionResultRepository;
    private final SessionParticipantRepository sessionParticipantRepository;
    private final ObjectMapper objectMapper;


    public ResultService(SessionRepository sessionRepository, SessionOptionRepository sessionOptionRepository, VoteRepository voteRepository, SessionResultRepository sessionResultRepository, SessionParticipantRepository sessionParticipantRepository, ObjectMapper objectMapper) {
        this.sessionRepository = sessionRepository;
        this.sessionOptionRepository = sessionOptionRepository;
        this.voteRepository = voteRepository;
        this.sessionResultRepository = sessionResultRepository;
        this.sessionParticipantRepository = sessionParticipantRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public ResultResponse calculateAndStoreResults(UUID sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session", sessionId.toString()));

        List<SessionOption> options = sessionOptionRepository.findBySessionIdOrderByCreatedAt(sessionId);
        List<Vote> votes = voteRepository.findBySessionId(sessionId);

        if (votes.isEmpty()) {
            throw new ValidationException("No votes casted in the session", "NO_VOTES");
        }

        ResultResponse results = switch (session.getVotingMode()) {
            case DEFAULT -> calculateDefaultResults(session, options, votes);
            case POINTS -> calculatePointsResults(session, options, votes);
            case RANKED -> calculateRankedResults(session, options, votes);
            case VETO -> calculateVetoResults(session, options, votes);
        };
        storeResults(session, results);
        logger.info("Results calculated for session: {}", session.getSessionCode());

        return results;
    }

    public ResultResponse getResults(UUID sessionId) {
        SessionResult result = sessionResultRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Results not available yet for session",
                        sessionId.toString()
                ));

        try {
            return objectMapper.convertValue(
                    result.getFullResults(),
                    ResultResponse.class
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse stored results", e);
        }
    }

    // ==================================================================
    // CALCULATION METHODS

    private ResultResponse calculateDefaultResults(Session session, List<SessionOption> options, List<Vote> votes) {
        Map<UUID, Integer> voteCounts = new HashMap<>();

        for (Vote vote : votes) {
            if (vote.getVoteValue() == 1) {
                voteCounts.merge(vote.getOption().getId(), 1, Integer::sum);
            }
        }

        return getResultResponse(session, options, votes, voteCounts, voteCounts);
    }

    private ResultResponse calculatePointsResults(Session session, List<SessionOption> options, List<Vote> votes) {
        Map<UUID, Integer> pointTotals = new HashMap<>();
        Map<UUID, Integer> voteCounts = new HashMap<>();

        for (Vote vote : votes) {
            UUID optionId = vote.getOption().getId();
            pointTotals.merge(optionId, vote.getVoteValue(), Integer::sum);
            voteCounts.merge(optionId, 1, Integer::sum);
        }

        // Build option results
        return getResultResponse(session, options, votes, pointTotals, voteCounts);
    }

    private ResultResponse calculateRankedResults(Session session, List<SessionOption> options, List<Vote> votes) {

        // Calculate weighted scores (1st place = n points, 2nd = n-1, etc.)
        int numOptions = options.size();
        Map<UUID, Integer> rankScores = new HashMap<>();
        Map<UUID, Integer> voteCounts = new HashMap<>();

        // Calculate weighted scores
        for (Vote vote : votes) {
            UUID optionId = vote.getOption().getId();
            int points = numOptions - vote.getVoteValue() + 1;
            rankScores.merge(optionId, points, Integer::sum);
            voteCounts.merge(optionId, 1, Integer::sum);
        }

        // Build option results
        return getResultResponse(session, options, votes, rankScores, voteCounts);
    }

    private ResultResponse calculateVetoResults(Session session, List<SessionOption> options, List<Vote> votes) {
        Map<UUID, Integer> voteCounts = new HashMap<>();
        Map<UUID, Integer> vetoCounts = new HashMap<>();

        // Count votes and vetoes
        for (Vote vote : votes) {
            UUID optionId = vote.getOption().getId();
            if (vote.getVeto()) {
                vetoCounts.merge(optionId, 1, Integer::sum);
            } else {
                voteCounts.merge(optionId, 1, Integer::sum);
            }
        }

        // Build results (score = votes - vetoes)
        List<ResultResponse.OptionResult> optionResults = options.stream()
                .map(option -> {
                    int voteCount = voteCounts.getOrDefault(option.getId(), 0);
                    int vetoCount = vetoCounts.getOrDefault(option.getId(), 0);
                    int score = voteCount - vetoCount;

                    ResultResponse.OptionResult result = new ResultResponse.OptionResult();
                    result.setOptionId(option.getId());
                    result.setOption(option.getOptionText());
                    result.setValue(score);
                    result.setVoteCount(voteCount);
                    result.setVetoCount(vetoCount);
                    return result;
                })
                .sorted(Comparator.comparing(ResultResponse.OptionResult::getValue).reversed())
                .collect(Collectors.toList());

        assignRanks(optionResults);
        return buildResultResponse(session, optionResults, votes.size());
    }

    // ========================================================================
    // HELPER METHODS
    // ========================================================================

    private ResultResponse getResultResponse(Session session, List<SessionOption> options, List<Vote> votes, Map<UUID, Integer> pointTotals, Map<UUID, Integer> voteCounts) {
        List<ResultResponse.OptionResult> optionResults = options.stream()
                .map(option -> {
                    int score = pointTotals.getOrDefault(option.getId(), 0);
                    long voteCount = voteCounts.getOrDefault(option.getId(), 0);

                    ResultResponse.OptionResult result = new ResultResponse.OptionResult();
                    result.setOptionId(option.getId());
                    result.setOption(option.getOptionText());
                    result.setValue(score);
                    result.setVoteCount((int) voteCount);
                    return result;
                })
                .sorted(Comparator.comparing(ResultResponse.OptionResult::getValue).reversed())
                .collect(Collectors.toList());

        assignRanks(optionResults);

        return buildResultResponse(session, optionResults, votes.size());
    }

    private void assignRanks(List<ResultResponse.OptionResult> results) {
        int currentRank = 1;
        Integer previousScore = null;

        for (int i = 0; i < results.size(); i++) {
            ResultResponse.OptionResult result = results.get(i);
            if (previousScore != null && !previousScore.equals(result.getValue())) {
                currentRank = i + 1;
            }
            result.setRank(currentRank);
            previousScore = result.getValue();
        }
    }

    private ResultResponse buildResultResponse(Session session,
                                               List<ResultResponse.OptionResult> optionResults,
                                               int totalVotes) {
        ResultResponse response = new ResultResponse();
        response.setSessionId(session.getId());
        response.setSessionCode(session.getSessionCode());
        response.setTitle(session.getTitle());
        response.setVoteMode(session.getVotingMode());

        // Participation stats
        int totalParticipants = sessionParticipantRepository.countBySessionId(session.getId());
        response.setTotalParticipants(totalParticipants);

        // Winner information
        if (!optionResults.isEmpty()) {
            ResultResponse.OptionResult winner = optionResults.getFirst();
            response.setWinnerOptionId(winner.getOptionId());
            response.setWinnerOption(winner.getOption());
            response.setWinnerScore(winner.getValue());
        }

        // Results
        response.setResults(optionResults);
        response.setSessionClosedAt(session.getClosedAt());

        return response;
    }

    private void storeResults(Session session, ResultResponse results) {
        try {
            Map<String, Object> fullResults = objectMapper.convertValue(results, Map.class);
            SessionResult sessionResult = sessionResultRepository.findBySessionId(session.getId())
                    .orElse(new SessionResult());

            if (results.getWinnerOptionId() != null) {
                sessionOptionRepository.findById(results.getWinnerOptionId())
                        .ifPresent(sessionResult::setWinnerOption);
                sessionResult.setWinnerOptionText(results.getWinnerOption());
                sessionResult.setWinnerScore(results.getWinnerScore());
            }

            // Set full results as JSONB
            sessionResult.setFullResults(fullResults);

            // Set participation stats
            sessionResult.setTotalVotesCast(results.getTotalVotes());
            sessionResult.setTotalParticipants(results.getTotalParticipants());

            sessionResultRepository.save(sessionResult);

        } catch (Exception e) {
            logger.error("Failed to persist results for session: {}", session.getId(), e);
            throw new RuntimeException("Failed to store results", e);
        }
    }
}
