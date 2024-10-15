package com.election.backendjava.repositories;

import com.election.backendjava.models.election.Votes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotesRepository extends JpaRepository<Votes, Long> {
}
