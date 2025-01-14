package com.election.backendjava.services.user;

import com.election.backendjava.models.user.ERole;
import com.election.backendjava.models.user.Role;
import com.election.backendjava.models.user.User;
import com.election.backendjava.repositories.election.RoleRepository;
import com.election.backendjava.repositories.user.UserRepository;
import com.election.backendjava.security.services.UserDetailsImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServices {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServices(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public User getUserWithComments(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.getComments().size();

        return user;
    }

    public User getUserFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid user authentication");
        }

        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Transactional
    public void banUser(Long userId, LocalDateTime expiration) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setBanned(true);
        user.setBanExpiration(expiration);
        user.setBanCount(user.getBanCount() + 1);
        userRepository.save(user);
    }

    @Transactional
    public void addAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Admin role not found"));

        user.getRoles().removeIf(r -> r.getName() == ERole.ROLE_USER);
        user.getRoles().add(adminRole);

        userRepository.save(user);
    }

    @Transactional
    public void demoteAdminToUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("User role not found"));

        user.getRoles().removeIf(r -> r.getName() == ERole.ROLE_ADMIN);
        user.getRoles().add(userRole);

        userRepository.save(user);
    }

    @Transactional
    public void unbanExpiredUsers(LocalDateTime now) {
        List<User> users = userRepository.findAll(); // or a more specific query if needed
        for (User user : users) {
            if (user.getBanExpiration() != null && user.getBanExpiration().isBefore(now)) {
                user.setBanned(false);
                user.setBanExpiration(null);
            }
        }
        userRepository.saveAll(users);
    }


    @Transactional
    public void updateUserById(Long userId, String firstname, String lastname, String region) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(firstname);
        user.setRegion(region);

        userRepository.save(user);
    }


}
