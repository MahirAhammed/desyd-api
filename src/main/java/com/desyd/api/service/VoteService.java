package com.desyd.api.service;

import com.desyd.api.dto.request.SubmitVoteRequest;
import com.desyd.api.dto.response.VoteResponse;
import com.desyd.api.entity.*;
import com.desyd.api.enums.SessionStatus;
import com.desyd.api.enums.VotingMode;
import com.desyd.api.exception.ResourceNotFoundException;
import com.desyd.api.exception.UnauthorizedException;
import com.desyd.api.exception.ValidationException;
import com.desyd.api.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class VoteService {

    private static final Logger logger = LoggerFactory.getLogger(VoteService.class);

    private final int TOTAL_POINTS = 10;

    private final VoteRepository voteRepository;
    private final SessionRepository sessionRepository;
    private final SessionParticipantRepository sessionParticipantRepository;
    private final SessionOptionRepository sessionOptionRepository;
    private final UserRepository userRepository;

    public VoteService(VoteRepository voteRepository, SessionRepository sessionRepository, SessionParticipantRepository sessionParticipantRepository, SessionOptionRepository sessionOptionRepository, UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.sessionRepository = sessionRepository;
        this.sessionParticipantRepository = sessionParticipantRepository;
        this.sessionOptionRepository = sessionOptionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public VoteResponse submitVote(UUID sessionId, SubmitVoteRequest request, UUID userId) {
        User user = findUser(userId);
        Session session = findActiveSession(sessionId);
        SessionParticipant participant = findParticipant(sessionId, userId);

        if (participant.getHasVoted()) {
            voteRepository.deleteBySessionIdAndUserId(sessionId, userId);
        }

        Map<UUID, SessionOption> optionsById = findOptionsMap(sessionId);
        validateVotes(session, request, optionsById);

        List<Vote> votes = buildVotes(session, user, request, optionsById);
        voteRepository.saveAll(votes);

        participant.markVoted();
        sessionParticipantRepository.save(participant);

        logger.info("User {} voted in session {}", user.getEmail(), session.getSessionCode());
        return buildVoteResponse(request, optionsById);
    }

    public VoteResponse getUserVotes(UUID sessionId, UUID userId) {
        if (!sessionParticipantRepository.existsBySessionIdAndUserId(sessionId, userId)) {
            throw new UnauthorizedException("You are not a participant in this session");
        }

        List<Vote> votes = voteRepository.findBySessionIdAndUserId(sessionId, userId);
        boolean hasVeto = votes.stream().anyMatch(Vote::getVeto);

        return new VoteResponse(
                votes.size(),
                hasVeto,
                votes.stream()
                        .map(vote -> new VoteResponse.VoteDetail(
                                vote.getOption().getOptionText(),
                                vote.getVeto() ? -1 : vote.getVoteValue()
                        ))
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public void deleteUserVotes(UUID sessionId, UUID userId) {
        Session session = findActiveSession(sessionId);
        SessionParticipant participant = findParticipant(sessionId, userId);
        voteRepository.deleteBySessionIdAndUserId(sessionId, userId);
        participant.resetVote();
        sessionParticipantRepository.save(participant);

        logger.info("User {} deleted votes from session {}", userId, session.getSessionCode());
    }

    // ========================================================================
    // PRIVATE HELPERS

    private User findUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));
    }

    private Session findActiveSession(UUID sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session", sessionId.toString()));
        if (session.getStatus() != SessionStatus.ACTIVE) {
            throw new ValidationException("Session Closed", "SESSION_CLOSED");
        }
        return session;
    }

    private SessionParticipant findParticipant(UUID sessionId, UUID userId) {
        return sessionParticipantRepository.findBySessionIdAndUserId(sessionId, userId)
                .orElseThrow(() -> new UnauthorizedException("You are not part of the session, join to vote"));
    }

    private Map<UUID, SessionOption> findOptionsMap(UUID sessionId) {
        return sessionOptionRepository.findBySessionIdOrderByCreatedAt(sessionId)
                .stream()
                .collect(Collectors.toMap(SessionOption::getId, o -> o));
    }

    // ========================================================================
    // VOTE BUILDING

    private List<Vote> buildVotes(Session session, User user, SubmitVoteRequest request, Map<UUID, SessionOption> optionsById) {

        List<Vote> votes = request.getVotes().stream()
                .map(input -> createVote(session, user, containsOption(optionsById, input.getOptionId()), input.getValue(), false))
                .collect(Collectors.toCollection(ArrayList::new));

        if (request.getVetoOptionId() != null) {
            SessionOption vetoOption = containsOption(optionsById, request.getVetoOptionId());
            votes.add(createVote(session, user, vetoOption, 1, true));
        }

        return votes;
    }

    private Vote createVote(Session session, User user, SessionOption option, int value, boolean isVeto) {
        Vote vote = new Vote();
        vote.setSession(session);
        vote.setUser(user);
        vote.setOption(option);
        vote.setVoteValue(value);
        vote.setVeto(isVeto);
        return vote;
    }

    private SessionOption containsOption(Map<UUID, SessionOption> optionsById, UUID optionId) {
        SessionOption option = optionsById.get(optionId);
        if (option == null) {
            throw new ValidationException("Option not found in this session", "INVALID_OPTION");
        }
        return option;
    }

    // ========================================================================
    // RESPONSE BUILDING
    // ========================================================================

    private VoteResponse buildVoteResponse(SubmitVoteRequest request, Map<UUID, SessionOption> optionsById) {

        List<VoteResponse.VoteDetail> details = request.getVotes().stream()
                .map(v -> {
                    String text = optionsById.getOrDefault(v.getOptionId(), null) != null
                            ? optionsById.get(v.getOptionId()).getOptionText()
                            : "Unknown";
                    return new VoteResponse.VoteDetail(text, v.getValue());
                })
                .collect(Collectors.toList());

        return new VoteResponse(
                request.getVotes().size(),
                request.getVetoOptionId() != null,
                details
        );
    }

    // ========================================================================
    // VALIDATION

    private void validateVotes(Session session, SubmitVoteRequest request, Map<UUID, SessionOption> optionsById) {
        switch (session.getVotingMode()) {
            case DEFAULT -> validateDefaultVotes(request);
            case POINTS  -> validatePointsVotes(request);
            case RANKED  -> validateRankedVotes(request, optionsById);
            case VETO    -> validateVetoVotes(request, session);
        }
    }

    private void validateDefaultVotes(SubmitVoteRequest request) {
       if  (request.getVotes().size() != 1){
           throw new ValidationException("DEFAULT mode only allows one vote", "INVALID_VOTE_COUNT");
       }

       if (request.getVotes().getFirst().getValue() != 1){
           throw new ValidationException("DEFAULT mode vote value must be 1", "INVALID_VOTE_VALUE");
       }
    }

    private void validatePointsVotes(SubmitVoteRequest request) {
        request.getVotes().forEach(v -> {
            if (v.getValue() < 0 || v.getValue() > TOTAL_POINTS) {
                throw new ValidationException(
                        "POINTS mode vote must be between 0 and " + TOTAL_POINTS,
                        "INVALID_VOTE_VALUE"
                );
            }
        });

        int total = request.getVotes().stream().mapToInt(VoteInput::getValue).sum();
        if (total != TOTAL_POINTS) {
            throw new ValidationException(
                    "POINTS mode total must equal " + TOTAL_POINTS + " (current value: " + total + ")",
                    "INVALID_POINTS_TOTAL"
            );
        }
    }

    private void validateRankedVotes(SubmitVoteRequest request, Map<UUID, SessionOption> optionsById) {
        if (request.getVotes().size() != optionsById.size()) {
            throw new ValidationException(
                    "RANKED mode must rank all options",
                    "INCOMPLETE_RANKING"
            );
        }

        List<Integer> sortedRanks = request.getVotes().stream()
                .map(VoteInput::getValue)
                .sorted()
                .toList();

        boolean validSequence = IntStream.range(0, sortedRanks.size())
                .allMatch(i -> sortedRanks.get(i) == i + 1);

        if (!validSequence) {
            throw new ValidationException(
                    "RANKED mode ranks must be sequential starting from 1",
                    "INVALID_RANKING"
            );
        }
    }

    private void validateVetoVotes(SubmitVoteRequest request, Session session) {
        if (session.getVotingMode() != VotingMode.VETO) {
            throw new ValidationException("Veto only allowed in VETO voting mode", "INVALID_VETO");
        }
        if (request.getVotes().size() != 1) {
            throw new ValidationException(
                    "In VETO mode, you must vote for exactly one option",
                    "INVALID_VETO_VOTE_COUNT"
            );
        }
        if (request.getVotes().getFirst().getValue() != 1) {
            throw new ValidationException("In VETO mode, vote value must be 1", "INVALID_VOTE_VALUE");
        }
        if (request.getVetoOptionId() == null) {
            throw new ValidationException("In VETO mode, you must veto one option", "VETO_REQUIRED");
        }
        if (request.getVotes().getFirst().getOptionId().equals(request.getVetoOptionId())) {
            throw new ValidationException("Option voted for cannot be vetoed", "INVALID_VETO_TARGET");
        }
    }
}
