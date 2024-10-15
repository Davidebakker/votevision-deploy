package com.election.backendjava.repositories;

import com.election.backendjava.models.election.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long> {
}
