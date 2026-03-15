package com.desyd.api.service;

import com.desyd.api.dto.request.CreateSessionRequest;
import com.desyd.api.dto.response.OptionResponse;
import com.desyd.api.dto.response.SessionResponse;
import com.desyd.api.entity.*;
import com.desyd.api.enums.SessionStatus;
import com.desyd.api.exception.ConflictException;
import com.desyd.api.exception.ResourceNotFoundException;
import com.desyd.api.exception.UnauthorizedException;
import com.desyd.api.exception.ValidationException;
import com.desyd.api.repository.*;
import com.desyd.api.util.SessionCodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SessionService {
    private static final Logger logger = LoggerFactory.getLogger(SessionService.class);

    private final SessionRepository sessionRepository;
    private final SessionOptionRepository sessionOptionRepository;
    private final SessionParticipantRepository sessionParticipantRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final SessionCodeGenerator codeGenerator;
    private final ResultService resultService;

    public SessionService(SessionRepository sessionRepository, SessionOptionRepository sessionOptionRepository, SessionParticipantRepository sessionParticipantRepository, UserRepository userRepository, UserProfileRepository userProfileRepository, SessionCodeGenerator codeGenerator, ResultService resultService) {
        this.sessionRepository = sessionRepository;
        this.sessionOptionRepository = sessionOptionRepository;
        this.sessionParticipantRepository = sessionParticipantRepository;
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.codeGenerator = codeGenerator;
        this.resultService = resultService;
    }

    @Transactional
    public SessionResponse createSession(CreateSessionRequest request, UUID userId) {
        User user = findUserById(userId);
        // Build session
        Session session = sessionRepository.save(buildSession(request, user));
        // Create options
        List<SessionOption> options = request.getOptions().stream()
                .map(text -> buildOption(session, text, user))
                .collect(Collectors.toList());
        sessionOptionRepository.saveAll(options);

        addParticipant(session, user); // Host joins session automatically
        userProfileRepository.incrementSessionsHosted(userId);  // Increment hosted count

        logger.info("Session created: {} by host: {}", session.getSessionCode(), user.getEmail());
        return toSessionResponse(session, userId);
    }

    @Transactional
    public SessionResponse joinSession(String sessionCode, UUID userId) {
        User user = findUserById(userId);
        Session session = findSessionByCode(sessionCode);

        if (session.getStatus() == SessionStatus.CLOSED) {
            throw new ValidationException("Session Closed", "SESSION_CLOSED");
        }

        if (session.getStatus() == SessionStatus.ARCHIVED) {
            throw new ValidationException("Cannot join archived session", "SESSION_ARCHIVED");
        }

        // Validate join
        if (sessionParticipantRepository.existsBySessionIdAndUserId(session.getId(), user.getId())) {
            throw new ConflictException("Already this session", "ALREADY_JOINED");
        }

        // Check if session is full
        long participantCount = sessionParticipantRepository.countBySessionId(session.getId());
        if (participantCount >= session.getMaxParticipants()) {
            throw new ConflictException(
                    String.format("Session is full (%d/%d)", participantCount, session.getMaxParticipants()),
                    "SESSION_FULL"
            );
        }

        addParticipant(session, user);
        userProfileRepository.incrementSessionsJoined(userId);  // Increment joined count
        logger.info("User {} joined session: {}", user.getEmail(), sessionCode);

        return toSessionResponse(session, userId);
    }

    public SessionResponse getSessionByCode(String sessionCode, UUID userId) {
        Session session = findSessionByCode(sessionCode);

        // Verify user is participant
        if (!sessionParticipantRepository.existsBySessionIdAndUserId(session.getId(), userId)) {
            throw new UnauthorizedException("You are not a participant of this session");
        }

        return toSessionResponse(session, userId);
    }

    @Transactional
    public SessionResponse closeSession(UUID sessionId, UUID userId) {
        Session session = findSessionById(sessionId);

        // Check if the request is from the Host
        if (!session.getCreatedBy().getId().equals(userId)) {
            throw new UnauthorizedException("Only the session creator can close this session");
        }

        if (session.getStatus() == SessionStatus.CLOSED) {
            throw new ValidationException("Session is already closed", "SESSION_ALREADY_CLOSED");
        }

        session.setStatus(SessionStatus.CLOSED);
        session.setClosedAt(OffsetDateTime.now());
        session = sessionRepository.save(session);

        try {
            resultService.calculateAndStoreResults(sessionId);
        } catch (ValidationException e) {
            logger.warn("Session closed without votes: {}", session.getSessionCode());
        }

        logger.info("Session closed: {}", session.getSessionCode());
        return toSessionResponse(session, userId);
    }

    @Transactional
    public void deleteSession(UUID sessionId, UUID userId) {
        Session session = findSessionById(sessionId);

        // Only creator can delete
        if (!session.getCreatedBy().getId().equals(userId)) {
            throw new UnauthorizedException("Only the session creator can delete this session");
        }

        sessionRepository.delete(session);
        logger.info("Session deleted: {}", session.getSessionCode());
    }

    public List<SessionResponse> getUserSessions(UUID userId) {
        // Get all sessions user has joined
        List<SessionParticipant> sessions = sessionParticipantRepository.findByUserId(userId);

        return sessions.stream()
            .map(session -> toSessionResponse(session.getSession(), userId))
            .collect(Collectors.toList());
    }

    // =======================
    // PRIVATE HELPER METHODS

    private User findUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id.toString()));
    }

    private Session findSessionById(UUID id){
        return sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session", id.toString()));
    }

    private Session findSessionByCode(String code){
        return sessionRepository.findBySessionCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Session", code));
    }

    private Session buildSession(CreateSessionRequest request, User user) {
        Session session = new Session();
        session.setSessionCode(codeGenerator.generateUniqueCode());
        session.setTitle(request.getTitle());
        session.setDescription(request.getDescription());
        session.setCategory(request.getCategory());
        session.setVotingMode(request.getVotingMode());
        session.setMaxParticipants(request.getMaxParticipants());
        session.setAllowParticipantOptions(request.getAllowParticipantOptions());
        session.setAnonymousVoting(request.getAnonymousVoting());
        session.setShowLiveResults(request.getShowLiveResults());
        session.setDuration(request.getDuration());
        session.setCreatedBy(user);
        session.setStatus(SessionStatus.ACTIVE);
        return session;
    }

    private SessionOption buildOption(Session session, String optionText, User user) {
        SessionOption option = new SessionOption();
        option.setSession(session);
        option.setOptionText(optionText);
        option.setMetadata(null);
        option.setCreatedBy(user);
        return option;
    }

    private void addParticipant(Session session, User user) {
        SessionParticipant participant = new SessionParticipant();
        participant.setId(new SessionParticipantId(session.getId(), user.getId()));
        participant.setSession(session);
        participant.setUser(user);
        participant.setHasVoted(false);
        participant.setJoinedAt(OffsetDateTime.now());
        sessionParticipantRepository.save(participant);
    }

    // CONVERSIONS
    private SessionResponse toSessionResponse(Session session, UUID currentUserId) {
        SessionResponse response = new SessionResponse();
        response.setId(session.getId());
        response.setSessionCode(session.getSessionCode());
        response.setTitle(session.getTitle());
        response.setDescription(session.getDescription());
        response.setCategory(session.getCategory());
        response.setVoteMode(session.getVotingMode());
        response.setStatus(session.getStatus());
        response.setMaxParticipants(session.getMaxParticipants());
        response.setCurrentParticipantCount(sessionParticipantRepository.countBySessionId(session.getId()));
        response.setAllowParticipantOptions(session.getAllowParticipantOptions());
        response.setAnonymousVoting(session.getAnonymousVoting());
        response.setShowLiveResults(session.getShowLiveResults());
        response.setDuration(session.getDuration());
        response.setCreatedByUsername(session.getCreatedBy().getUserProfile().getUsername());
        response.setCreator(session.getCreatedBy().getId().equals(currentUserId));
        response.setCreatedAt(session.getCreatedAt());
        response.setClosedAt(session.getClosedAt());

        // Calculate expiry time
        if (session.getDuration() != null && session.getStatus() == SessionStatus.ACTIVE) {
            response.setExpiresAt(session.getCreatedAt().plusMinutes(session.getDuration()));
        }

        // Check if current user has voted
        sessionParticipantRepository.findBySessionIdAndUserId(session.getId(), currentUserId)
        .ifPresent(participant -> response.setHasVoted(participant.getHasVoted()));

        // Get options
        List<SessionOption> options = sessionOptionRepository.findBySessionIdOrderByCreatedAt(session.getId());
        response.setOptions(options.stream()
            .map(this::toOptionResponse)
            .collect(Collectors.toList()));

        return response;
    }

    private OptionResponse toOptionResponse(SessionOption option) {
        OptionResponse response = new OptionResponse();
        response.setId(option.getId());
        response.setOptionText(option.getOptionText());
        response.setAddedByUsername(option.getCreatedBy().getUserProfile().getUsername());
        response.setCreatedAt(option.getCreatedAt());
        return response;
    }
}
