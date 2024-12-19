package com.election.backendjava.security.services;
import com.election.backendjava.dto.PartyMunicipalityResultDTO;
import com.election.backendjava.repositories.election.ElectionResultRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ElectionService {

    @Autowired
    private ElectionResultRepository electionRepository;

    public List<PartyMunicipalityResultDTO> getNationalResults() {
        // Fetch national results from the repository
        return electionRepository.findNationalResults();
    }
}