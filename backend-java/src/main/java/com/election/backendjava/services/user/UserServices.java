package com.election.backendjava.services.user;

import com.election.backendjava.models.user.ERole;
import com.election.backendjava.models.user.Role;
import com.election.backendjava.models.user.User;
import com.election.backendjava.repositories.election.RoleRepository;
import com.election.backendjava.repositories.user.UserRepository;
import com.election.backendjava.security.services.UserDetailsImpl;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServices {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServices(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User getUserFromAuthentication() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid user authentication");
            }

            return userRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching user from authentication: " + e.getMessage(), e);
        }
    }

    @Transactional
    public User getUserWithComments(Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.getComments().size();

            return user;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching user with comments: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void banUser(Long userId, LocalDateTime expiration) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setBanned(true);
            user.setBanExpiration(expiration);
            user.setBanCount(user.getBanCount() + 1);
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error banning user: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void addAdmin(Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));

            user.getRoles().removeIf(r -> r.getName() == ERole.ROLE_USER);
            user.getRoles().add(adminRole);

            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error adding admin role: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void demoteAdminToUser(Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("User role not found"));

            user.getRoles().removeIf(r -> r.getName() == ERole.ROLE_ADMIN);
            user.getRoles().add(userRole);

            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error demoting admin to user: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void unbanExpiredUsers(LocalDateTime now) {
        try {
            List<User> users = userRepository.findAll();
            for (User user : users) {
                if (user.getBanExpiration() != null && user.getBanExpiration().isBefore(now)) {
                    user.setBanned(false);
                    user.setBanExpiration(null);
                }
            }
            userRepository.saveAll(users);
        } catch (Exception e) {
            throw new RuntimeException("Error unbanning expired users: " + e.getMessage(), e);
        }
    }

    @Transactional
    public boolean unbanUser(Long userId) {
        try {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setBanned(false);
                user.setBanExpiration(null);
                userRepository.save(user);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error unbanning user: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ResponseEntity<?> updateUserById(Long userId, String username, String email, String firstname, String region) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.setUsername(username);
            user.setEmail(email);
            user.setName(firstname);
            user.setRegion(region);
            userRepository.save(user);

            return ResponseEntity.ok("User was saved successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating user: " + e.getMessage());
        }
    }
}
