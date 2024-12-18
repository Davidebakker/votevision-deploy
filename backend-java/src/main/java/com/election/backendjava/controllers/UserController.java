package com.election.backendjava.controllers;

import com.election.backendjava.models.user.EBanPeriod;
import com.election.backendjava.models.user.User;
import com.election.backendjava.payload.response.MessageResponse;
import com.election.backendjava.repositories.election.RoleRepository;
import com.election.backendjava.repositories.user.UserRepository;
import com.election.backendjava.security.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/findAll/{userRole}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<?> getAllUsersByRole(@PathVariable String userRole) {
        List<User> users = userRepository.findAllByRoleName(userRole.toUpperCase());

        return ResponseEntity.ok(users);
    }

    @GetMapping("/getDetails")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid user authentication");
        }

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return ResponseEntity.ok(userRepository.findById(user.getUserId()));
    }

    @PostMapping("/add/admin/{userId}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> addAdmin(@PathVariable Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        userRepository.addAdmin(userId);


        return ResponseEntity.ok(new MessageResponse("Admin added Successfully"));
    }

    @PostMapping("/demote/admin/{userId}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        userRepository.demoteAdminToUser(userId);

        return ResponseEntity.ok(new MessageResponse("Admin deleted Successfully"));
    }

    @PostMapping("/delete/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        userRepository.delete(user);

        return ResponseEntity.ok(new MessageResponse("User deleted successfully"));
    }

    @PostMapping("/ban/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<?> banUser(@PathVariable Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        int banAmount = userRepository.findBanAmountByUserId(userId);

        EBanPeriod banPeriod = EBanPeriod.getNextPeriod(banAmount);

        int updatedRows = userRepository.banUser(userId, LocalDateTime.now().plusDays(banPeriod.getDays()));
        if (updatedRows > 0) {
            return ResponseEntity.ok(new MessageResponse("User banned successfully"));
        } else {
            return ResponseEntity.ok(new MessageResponse("An error occurred while banning the user"));
        }
    }
}
