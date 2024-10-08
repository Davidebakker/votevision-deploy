package com.election.backendjava.controllers;

import com.election.backendjava.repositories.UserRepository;
import com.election.backendjava.models.User;
import com.election.backendjava.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
    }

    @GetMapping("/getAll")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/getById/{id}")
    public User getUserById(@PathVariable long id) {
        logger.info("Get user with email: {}", id);

        return userService.getUserById(id);
    }

//    @GetMapping("/getByEmail/{email}")
//    public User getUserByEmail(@PathVariable String email) {
//        logger.info("Get user with email: {}", email);
//
//        return userService.getUserByEmail(email);
//    }

    @GetMapping("/getByEmail/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        logger.info("Get user with email: {}", email);

        try {
            User user = userService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(500).body("Failed to find user with email: " + email);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        logger.info("Received data: {}", user.toString());

        try {
            userService.createUser(user);
            return ResponseEntity.ok("User saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to save user");
        }
    }
}


