package com.desyd.api.service;

import com.desyd.api.dto.request.LoginRequest;
import com.desyd.api.dto.request.RefreshTokenRequest;
import com.desyd.api.dto.request.RegisterRequest;
import com.desyd.api.dto.response.LoginResponse;
import com.desyd.api.dto.response.UserProfileResponse;
import com.desyd.api.entity.User;
import com.desyd.api.entity.UserProfile;
import com.desyd.api.exception.DuplicateResourceException;
import com.desyd.api.exception.ResourceNotFoundException;
import com.desyd.api.exception.ValidationException;
import com.desyd.api.repository.UserProfileRepository;
import com.desyd.api.repository.UserRepository;
import com.desyd.api.security.JwtUtil;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    public UserService(UserRepository userRepository,
                       UserProfileRepository userProfileRepository,
                       JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.encoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public UserProfileResponse register(RegisterRequest request){

        if (userRepository.existsByEmail(request.getEmail())){
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }

        if (userProfileRepository.existsByUsername(request.getUsername())){
            throw new DuplicateResourceException("User", "email", request.getUsername());
        }

        User user = createUser(request);
        UserProfile profile = createProfile(user, request);

        logger.info("User registered successfully: {}", user.getEmail());
        return toUserProfileResponse(profile);
    }

    @Transactional
    public LoginResponse login(LoginRequest request){

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // Successful login
            User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.getEmail()));

            if (!user.getActive()){
                throw new ValidationException("Account is deactivated", "ACCOUNT_DEACTIVATED");
            }

            UserProfile profile = userProfileRepository.findById(user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Profile"));

            // Generate tokens
            String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getEmail());
            String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getEmail());

            logger.info("User logged in successfully: {}", user.getEmail());
            return new LoginResponse(
                    accessToken,
                    refreshToken,
                    jwtExpiration,
                    toUserProfileResponse(profile)
            );


        } catch (AuthenticationException e) {
            logger.warn("Login failed for email: {}", request.getEmail());
            throw new ValidationException("Invalid email or password", "INVALID_CREDENTIALS");
        }
    }

    public LoginResponse refreshToken(RefreshTokenRequest request) {
        try {
            String refreshToken = request.getRefreshToken();

            // Validate refresh token
            if (jwtUtil.isTokenExpired(refreshToken)) {
                throw new com.desyd.api.exception.AuthenticationException("Refresh token expired", "TOKEN_EXPIRED");
            }

            UUID userId = jwtUtil.extractUserId(refreshToken);
            String email = jwtUtil.extractEmail(refreshToken);

            // Verify user exists and is active
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));

            if (!user.getActive()) {
                throw new com.desyd.api.exception.AuthenticationException("Account is deactivated", "ACCOUNT_DEACTIVATED");
            }

            // Generate new tokens
            String newAccessToken = jwtUtil.generateAccessToken(userId, email);
            String newRefreshToken = jwtUtil.generateRefreshToken(userId, email);

            UserProfile profile = userProfileRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));

            logger.info("Tokens refreshed for user: {}", email);

            return new LoginResponse(
                    newAccessToken,
                    newRefreshToken,
                    jwtExpiration,
                    toUserProfileResponse(profile)
            );

        } catch (Exception e) {
            logger.error("Error refreshing token", e);
            throw new com.desyd.api.exception.AuthenticationException("Invalid refresh token", "INVALID_TOKEN");
        }
    }

    public UserProfileResponse getUserProfile(UUID userId) {
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));
        return toUserProfileResponse(profile);
    }

    public void logout(UUID id){
        userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id.toString()));

        logger.info("User logged out: {}", id);
    }

    private User createUser(RegisterRequest request){
        User user =  new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(encoder.encode(request.getPassword()));
        user.setEmailVerified(false);
        user.setActive(true);

        return userRepository.save(user);
    }

    private UserProfile createProfile(User user, RegisterRequest request){
        UserProfile profile = new UserProfile();
        profile.setUser(user);
        profile.setUsername(request.getUsername());
        profile.setSessionsHosted(0);
        profile.setSessionsJoined(0);
        return userProfileRepository.save(profile);
    }

    private UserProfileResponse toUserProfileResponse(UserProfile profile){
        UserProfileResponse response = new UserProfileResponse();
        response.setId(profile.getUser().getId());
        response.setUsername(profile.getUsername());
        response.setEmail(profile.getUser().getEmail());
        response.setSessionsJoined(profile.getSessionsJoined());
        response.setSessionsHosted(profile.getSessionsHosted());
        return response;
    }

}
