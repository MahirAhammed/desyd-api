package com.desyd.api.service;

import com.desyd.api.dto.request.LoginRequestDTO;
import com.desyd.api.dto.request.RegisterRequestDTO;
import com.desyd.api.dto.response.UserProfileResponse;
import com.desyd.api.entity.User;
import com.desyd.api.entity.UserProfile;
import com.desyd.api.repository.UserProfileRepository;
import com.desyd.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, UserProfileRepository userProfileRepository) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.encoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public UserProfileResponse register(RegisterRequestDTO request){

        if (userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already registered");
        }

        if (userProfileRepository.existsByUsername(request.getUsername())){
            throw new RuntimeException("Username already exists");
        }

        User user = createUser(request);
        UserProfile profile = createProfile(user, request);

        return toUserProfileResponse(profile);
    }

    @Transactional
    public UserProfileResponse login(LoginRequestDTO request){

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!user.getActive()){
            throw new RuntimeException("Account is deactivated");
        }

        if(!encoder.matches(request.getPassword(), user.getPasswordHash())){
            throw new RuntimeException("Invalid email or password");
        }

        UserProfile profile = userProfileRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return toUserProfileResponse(profile);
    }

    private User createUser(RegisterRequestDTO request){
        User user =  new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(encoder.encode(request.getPassword()));
        user.setEmailVerified(false);
        user.setActive(true);

        return userRepository.save(user);
    }

    private UserProfile createProfile(User user, RegisterRequestDTO request){
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
