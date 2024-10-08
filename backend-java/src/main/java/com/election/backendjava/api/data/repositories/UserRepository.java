package com.election.backendjava.api.data.repositories;

import com.election.backendjava.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
