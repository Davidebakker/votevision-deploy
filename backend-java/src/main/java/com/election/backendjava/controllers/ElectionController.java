package com.election.backendjava.controllers;

import com.election.backendjava.dto.PartyMunicipalityResultDTO;
import com.election.backendjava.models.election.Candidate;
import com.election.backendjava.models.election.Party;
import com.election.backendjava.payload.response.MessageResponse;
import com.election.backendjava.repositories.election.CandidateRepository;
import com.election.backendjava.repositories.election.PartyRepository;
import com.election.backendjava.repositories.election.VotesRepository;
import com.election.backendjava.services.election.ElectionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/elections")
public class ElectionController {

    private final PartyRepository partyRepository;
    private final CandidateRepository candidateRepository;
    private final VotesRepository votesRepository;
    private final ElectionService electionService;

    public ElectionController(PartyRepository partyRepository, CandidateRepository candidateRepository, VotesRepository votesRepository, ElectionService electionService) {
        this.partyRepository = partyRepository;
        this.candidateRepository = candidateRepository;
        this.votesRepository = votesRepository;
        this.electionService = electionService;
    }

    // Endpoint om alle partijen op te halen
    @GetMapping("/parties")
    public List<Party> getAllParties() {
        return partyRepository.findAll();
    }

    @GetMapping("/party/{partyName}")
    public ResponseEntity<?> getParty(@PathVariable String partyName) {
        System.out.println("Searching for party: " + partyName);
        Optional<Party> getParty = partyRepository.findByNameIgnoreCase(partyName);

        if (getParty.isPresent()) {
            System.out.println("Party found: " + getParty.get());
            return ResponseEntity.ok(getParty.get());
        } else {
            System.out.println("Party not found: " + partyName);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Party not found with name: " + partyName));
        }
    }

    @GetMapping("/candidate/{candidateName}")
    public ResponseEntity<?> getPartyCandidate(@PathVariable String candidateName) {
        Optional<Candidate> getCandidate = candidateRepository.findByLastNameIgnoreCase(candidateName);

        if (getCandidate.isPresent()) {
            return ResponseEntity.ok(getCandidate.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Party not found with name: " + candidateName));
        }
    }

    @GetMapping("/candidate")
    public ResponseEntity<List<Candidate>> getAllCandidates() {
        return ResponseEntity.ok(candidateRepository.findAll());
    }

    @GetMapping("/results")
    public ResponseEntity<List<PartyMunicipalityResultDTO>> getNationalResults() {
        List<PartyMunicipalityResultDTO> results = electionService.getNationalResults();
        return ResponseEntity.ok(results);
    }


}
