package com.election.backendjava.repositories.user;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Query(value = "SELECT u.* " +
            "FROM users u " +
            "JOIN user_roles ur ON u.user_id = ur.user_id " +
            "JOIN roles r ON ur.role_id = r.role_id " +
            "WHERE r.name = :roleName",
            nativeQuery = true)
    List<User> findAllByRoleName(@Param("roleName") String roleName);


    @Modifying
    @Transactional
    @Query(value = "UPDATE User u SET u.banned = true," +
            " u.banExpiration = :expiration, u.banCount = u.banCount + 1 WHERE u.userId = :userId")
    int banUser(@Param("userId") Long userId, @Param("expiration") LocalDateTime expiration);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_roles " +
            "SET role_id = (SELECT role_id FROM roles WHERE name = 'ROLE_ADMIN') " +
            "WHERE user_id = :userId " +
            "AND role_id = (SELECT role_id FROM roles WHERE name = 'ROLE_USER')",
            nativeQuery = true)
    void addAdmin(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_roles " +
            "SET role_id = (SELECT role_id FROM roles WHERE name = 'ROLE_USER') " +
            "WHERE user_id = :userId " +
            "AND role_id = (SELECT role_id FROM roles WHERE name = 'ROLE_ADMIN')",
            nativeQuery = true)
    void demoteAdminToUser(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE User u SET u.banned = false, u.banExpiration = null WHERE u.banExpiration <= :now")
    void unbanExpiredUsers(@Param("now") LocalDateTime now);

    @Query(value = "SELECT u.banCount FROM User u WHERE u.userId = :userId")
    int findBanAmountByUserId(@Param("userId") Long userId);
}
