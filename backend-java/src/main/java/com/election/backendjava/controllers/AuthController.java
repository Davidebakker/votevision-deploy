package com.election.backendjava.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.election.backendjava.services.user.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.election.backendjava.models.user.ERole;
import com.election.backendjava.models.user.Role;
import com.election.backendjava.models.user.User;
import com.election.backendjava.payload.request.LoginRequest;
import com.election.backendjava.payload.request.RegisterRequest;
import com.election.backendjava.payload.response.JwtResponse;
import com.election.backendjava.payload.response.MessageResponse;
import com.election.backendjava.repositories.election.RoleRepository;
import com.election.backendjava.repositories.user.UserRepository;
import com.election.backendjava.repositories.CookieRepository;
import com.election.backendjava.security.jwt.JwtUtils;
import com.election.backendjava.security.services.UserDetailsImpl;

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