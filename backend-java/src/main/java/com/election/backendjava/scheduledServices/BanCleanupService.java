package com.election.backendjava.scheduledServices;

import com.election.backendjava.repositories.user.UserRepository;
import com.election.backendjava.services.user.UserServices;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BanCleanupService {

    private final UserServices userServices;

    public BanCleanupService(UserServices userServices) {
        this.userServices = userServices;
    }

    @Scheduled(fixedRate = 300000)
    @Transactional
    public void unbanExpiredUsers() {
        userServices.unbanExpiredUsers(LocalDateTime.now());
    }
}

