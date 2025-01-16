package com.election.backendjava.controllers;

import com.election.backendjava.models.user.EBanPeriod;
import com.election.backendjava.models.user.ERole;
import com.election.backendjava.models.user.User;
import com.election.backendjava.payload.request.EditUserRequest;
import com.election.backendjava.payload.response.MessageResponse;
import com.election.backendjava.repositories.election.RoleRepository;
import com.election.backendjava.repositories.user.UserRepository;
import com.election.backendjava.services.user.UserServices;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;
    private final UserServices userServices;

    public UserController(UserRepository userRepository, UserServices userServices, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userServices = userServices;
    }

    @GetMapping("/findAll/{userRole}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<?> getAllUsersByRole(@PathVariable String userRole) {
        ERole role = ERole.valueOf(userRole.toUpperCase());
        List<User> users = userRepository.findAllByRoles_Name(role);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/getDetails")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserDetails() {
        User user = userServices.getUserFromAuthentication();
        return ResponseEntity.ok(userServices.getUserWithComments(user.getUserId()));
    }

    @PostMapping("/add/admin/{userId}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> addAdmin(@PathVariable Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        userServices.addAdmin(userId);


        return ResponseEntity.ok(new MessageResponse("Admin added Successfully"));
    }

    @PostMapping("/demote/admin/{userId}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        userServices.demoteAdminToUser(userId);

        return ResponseEntity.ok(new MessageResponse("Admin deleted Successfully"));
    }

    @PostMapping("/disable/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setActive(false);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User disabled successfully"));
    }

    @PostMapping("/ban/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<?> banUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        int banAmount = user.getBanCount();

        EBanPeriod banPeriod = EBanPeriod.getNextPeriod(banAmount);

        try {
            userServices.banUser(userId, LocalDateTime.now().plusDays(banPeriod.getDays()));
            return ResponseEntity.ok(new MessageResponse("User banned successfully"));
        } catch (Exception e) {
            return ResponseEntity.ok(new MessageResponse("An error occurred while banning the user"));
        }
    }

    @PutMapping("/unban/{userId}")
    public ResponseEntity<?> unbanUser(@PathVariable Long userId) {
        if (userServices.unbanUser(userId)) {
            return ResponseEntity.ok(new MessageResponse("User unbanned successfully"));

        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("An error occurred while unbanning the user"));
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editUser(@RequestBody EditUserRequest e) {
        return userServices.updateUserById(
                e.getUserId(),
                e.getUsername(),
                e.getEmail(),
                e.getName(),
                e.getRegion());
    }
}
