package com.election.backendjava.services.election;

import com.election.backendjava.dto.PartyMunicipalityResultDTO;
import com.election.backendjava.repositories.election.ElectionResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ElectionService {
    private final ElectionResultRepository electionRepository;

    public ElectionService(ElectionResultRepository electionRepository) {
        this.electionRepository = electionRepository;
    }

    public List<PartyMunicipalityResultDTO> getNationalResults() {
        // Fetch national results from the repository
        return electionRepository.findNationalResults();
    }
}