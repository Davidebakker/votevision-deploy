package com.election.backendjava.scheduledServices;

import com.election.backendjava.repositories.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BanCleanupService {

    private final UserRepository userRepository;

    public BanCleanupService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(fixedRate = 300000)
    @Transactional
    public void unbanExpiredUsers() {
        userRepository.unbanExpiredUsers(LocalDateTime.now());
    }
}

