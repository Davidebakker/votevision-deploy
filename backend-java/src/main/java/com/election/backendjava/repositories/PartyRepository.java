package com.election.backendjava.repositories;

import com.election.backendjava.models.election.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long> {
}
