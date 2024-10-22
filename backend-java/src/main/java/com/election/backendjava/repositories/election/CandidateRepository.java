package com.election.backendjava.repositories.election;

import com.election.backendjava.models.election.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    public <T> Optional<T> findByLastNameIgnoreCase(String username);
}
