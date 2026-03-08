package com.desyd.api.controller;

import com.desyd.api.dto.request.SubmitVoteRequest;
import com.desyd.api.dto.response.VoteResponse;
import com.desyd.api.entity.Vote;
import com.desyd.api.service.VoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sessions/{sessionId}/votes")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping()
    public ResponseEntity<VoteResponse> submitVote(
            @PathVariable UUID sessionId,
            @Valid @RequestBody SubmitVoteRequest request,
            Authentication authentication
            ){

        UUID userId = (UUID) authentication.getPrincipal();
        VoteResponse response = voteService.submitVote(sessionId, request, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<VoteResponse> getMyVotes(@PathVariable UUID sessionId, Authentication authentication) {

        UUID userId = (UUID) authentication.getPrincipal();
        VoteResponse votes = voteService.getUserVotes(sessionId, userId);
        return ResponseEntity.ok(votes);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMyVotes(@PathVariable UUID sessionId, Authentication authentication) {

        UUID userId = (UUID) authentication.getPrincipal();
        voteService.deleteUserVotes(sessionId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
