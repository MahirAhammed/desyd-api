package com.desyd.api.controller;

import com.desyd.api.dto.request.LoginRequestDTO;
import com.desyd.api.dto.request.RegisterRequestDTO;
import com.desyd.api.dto.response.UserProfileResponse;
import com.desyd.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserProfileResponse> registerUser(@Valid @RequestBody RegisterRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<UserProfileResponse> loginUser(@Valid @RequestBody LoginRequestDTO request){
        return ResponseEntity.status(HttpStatus.OK).body(userService.login(request));
    }
}
