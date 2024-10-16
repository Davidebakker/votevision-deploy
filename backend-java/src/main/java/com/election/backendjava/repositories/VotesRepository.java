package com.election.backendjava.repositories;

import com.election.backendjava.models.election.Votes;
import com.election.backendjava.models.id.CandidateId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface VotesRepository extends JpaRepository<Votes, Long> {
    @Query("SELECT SUM(v.amount) FROM Votes v WHERE v.candidate.candidateId = :candidateId")
    Long getTotalVotesByCandidateId(@Param("candidateId") CandidateId candidateId);

    @Query("SELECT SUM(v.amount) FROM Votes v WHERE v.candidate.candidateId.partyId = :partyId")
    Long getTotalVotesByPartyId(@Param("partyId") int partyId);

}
