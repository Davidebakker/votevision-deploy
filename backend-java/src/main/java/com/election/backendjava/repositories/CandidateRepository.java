package com.election.backendjava.repositories;

import com.election.backendjava.models.election.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
}
