package com.election.backendjava.api.controllers;

import com.election.backendjava.api.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test");
    }

    @PostMapping("/data")
    public ResponseEntity<String> receiveData(@RequestBody User user) {
        logger.info("Received data: {}", user.toString());
        return ResponseEntity.ok("Data received successfully");
    }
}

