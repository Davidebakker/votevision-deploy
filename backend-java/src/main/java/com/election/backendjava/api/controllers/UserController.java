package com.election.backendjava.api.controllers;

import com.election.backendjava.api.data.repositories.UserRepository;
import com.election.backendjava.api.models.User;
import com.election.backendjava.api.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("get");
    }

    @PostMapping("/create")
    public ResponseEntity<String> receiveData(@RequestBody User user) {
        logger.info("Received data: {}", user.toString());

        try {
            userService.createUser(user);
            return ResponseEntity.ok("User saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to save user");
        }
    }
}

