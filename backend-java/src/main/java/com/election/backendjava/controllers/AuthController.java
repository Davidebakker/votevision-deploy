package com.election.backendjava.controllers;

import com.election.backendjava.services.user.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.election.backendjava.payload.request.LoginRequest;
import com.election.backendjava.payload.request.RegisterRequest;
import com.election.backendjava.payload.response.MessageResponse;

@SuppressWarnings("DuplicatedCode")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    private AuthController (AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return authService.authenticateUser(loginRequest, response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest signUpRequest, HttpServletResponse response) {
        return authService.registerUser(signUpRequest, response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> signOut(HttpServletResponse response) {
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok(new MessageResponse("Signed out successfully!"));
    }

}