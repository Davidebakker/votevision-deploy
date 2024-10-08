package com.election.backendjava.api.controllers;

import com.election.backendjava.api.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        // Process user data, for example, save it to a database (not shown here)
        System.out.println("Received user data: " + user.getName());

        // Return success response
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }
}

