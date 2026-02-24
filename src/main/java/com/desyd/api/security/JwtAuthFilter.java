package com.desyd.api.security;

import com.desyd.api.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    public JwtAuthFilter(JwtUtil jwtUtil, UserRepository repository) {
        this.jwtUtil = jwtUtil;
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = extractJwtFromRequest(request);

            if (jwt != null && !jwtUtil.isTokenExpired(jwt)) {
                UUID userId = jwtUtil.extractUserId(jwt);

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Verify user exists and is active
                    repository.findById(userId).ifPresent(user -> {
                        if (user.getActive() && jwtUtil.validateToken(jwt, userId)) {
                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(
                                            userId,
                                            null,
                                            new ArrayList<>()
                                    );
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    });
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
