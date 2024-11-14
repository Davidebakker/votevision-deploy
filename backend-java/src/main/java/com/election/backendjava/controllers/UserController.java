package com.election.backendjava.controllers;

import com.election.backendjava.models.election.Party;
import com.election.backendjava.models.user.User;
import com.election.backendjava.payload.response.MessageResponse;
import com.election.backendjava.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/findAll")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
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

        int updatedRows = userRepository.banUser(userId);
        if (updatedRows > 0) {
            return ResponseEntity.ok(new MessageResponse("User banned successfully"));
        } else {
            return ResponseEntity.ok(new MessageResponse("An error occurred while banning the user"));
        }
    }
}
