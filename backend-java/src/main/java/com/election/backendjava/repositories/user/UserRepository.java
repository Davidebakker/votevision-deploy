package com.election.backendjava.repositories.user;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.election.backendjava.models.user.ERole;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.election.backendjava.models.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    User findByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findAllByRoleName(@Param("roleName") ERole roleName);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.banned = true, u.banExpiration = :expiration, u.banCount = u.banCount + 1 WHERE u.userId = :userId")
    int banUser(@Param("userId") Long userId, @Param("expiration") LocalDateTime expiration);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.banned = false, u.banExpiration = null WHERE u.banExpiration <= :now")
    void unbanExpiredUsers(@Param("now") LocalDateTime now);

    @Query("SELECT u.banCount FROM User u WHERE u.userId = :userId")
    int findBanAmountByUserId(@Param("userId") Long userId);
}
