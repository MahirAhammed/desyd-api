package com.desyd.api.controller;

import com.desyd.api.dto.request.CreateSessionRequest;
import com.desyd.api.dto.request.JoinSessionRequest;
import com.desyd.api.dto.response.SessionResponse;
import com.desyd.api.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/create")
    public ResponseEntity<SessionResponse> createSession(@Valid @RequestBody CreateSessionRequest request, Authentication authentication) {
        UUID userId = (UUID) authentication.getPrincipal();
        SessionResponse response = sessionService.createSession(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/join")
    public ResponseEntity<SessionResponse> joinSession(@Valid @RequestBody JoinSessionRequest request, Authentication authentication) {
        UUID userId = (UUID) authentication.getPrincipal();
        SessionResponse response = sessionService.joinSession(request.getSessionCode(), userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{sessionCode}")
    public ResponseEntity<SessionResponse> getSession(@PathVariable String sessionCode, Authentication authentication) {
        UUID userId = (UUID) authentication.getPrincipal();
        SessionResponse response = sessionService.getSessionByCode(sessionCode, userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{sessionId}/close")
    public ResponseEntity<SessionResponse> closeSession(@PathVariable UUID sessionId, Authentication authentication) {
        UUID userId = (UUID) authentication.getPrincipal();
        SessionResponse response = sessionService.closeSession(sessionId, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteSession(@PathVariable UUID sessionId, Authentication authentication) {
        UUID userId = (UUID) authentication.getPrincipal();
        sessionService.deleteSession(sessionId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/my-sessions")
    public ResponseEntity<List<SessionResponse>> getUserSessions(Authentication authentication) {
        UUID userId = (UUID) authentication.getPrincipal();
        List<SessionResponse> sessions = sessionService.getUserSessions(userId);
        return ResponseEntity.ok(sessions);
    }
}
