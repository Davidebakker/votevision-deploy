
package com.election.backendjava.repositories.election;

import com.election.backendjava.dto.PartyMunicipalityResultDTO;
import com.election.backendjava.models.election.ElectionResult;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("electionResultRepository")
public interface ElectionResultRepository extends CrudRepository<ElectionResult, Long> {

    @Query("SELECT new com.election.backendjava.dto.PartyMunicipalityResultDTO(p.name, m.name, SUM(e.votesReceived)) " +
            "FROM ElectionResult e " +
            "JOIN e.party p " +
            "JOIN e.municipality m " +
            "GROUP BY p.name, m.name")
    List<PartyMunicipalityResultDTO> findNationalResults();
}