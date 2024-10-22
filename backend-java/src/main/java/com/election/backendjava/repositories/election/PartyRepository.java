package com.election.backendjava.repositories.election;

import com.election.backendjava.models.election.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartyRepository extends JpaRepository<Party, Long> {
    public <T> Optional<T> findByNameIgnoreCase(String name);
}
