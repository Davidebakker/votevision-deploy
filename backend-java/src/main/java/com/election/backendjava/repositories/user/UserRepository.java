package com.election.backendjava.repositories.user;


import java.util.List;
import java.util.Optional;

import com.election.backendjava.models.user.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.election.backendjava.models.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    List<User> findAllByRoles_Name(ERole roles_name);

}
