package com.desyd.api.security;

import com.desyd.api.exception.UnauthorizedException;
import com.desyd.api.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository repository;
    private final ObjectMapper objectMapper;

    public JwtAuthFilter(JwtUtil jwtUtil, UserRepository repository, ObjectMapper objectMapper) {
        this.jwtUtil = jwtUtil;
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = extractJwtFromRequest(request);

            if (jwt != null) {
                UUID userId = jwtUtil.extractUserId(jwt);

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    boolean authenticated = repository.findById(userId)
                            .map(user -> user.getActive() && jwtUtil.validateToken(jwt, userId))
                            .orElse(false);

                    if (authenticated) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        // Token was present but invalid
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        response.getWriter().write(objectMapper.writeValueAsString(new UnauthorizedException("Invalid or expired token")));
                        return;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication", e);
        }

        filterChain.doFilter(request, response);   // Continue to next filter or controller
    }

    private String extractJwtFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
