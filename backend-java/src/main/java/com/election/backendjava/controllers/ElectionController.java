package com.election.backendjava.controllers;

import com.election.backendjava.models.election.Candidate;
import com.election.backendjava.models.election.Party;
import com.election.backendjava.payload.response.MessageResponse;
import com.election.backendjava.repositories.CandidateRepository;
import com.election.backendjava.repositories.PartyRepository;
import com.election.backendjava.repositories.StationRepository;
import com.election.backendjava.repositories.VotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/elections")
public class ElectionController {

    @Autowired
    PartyRepository partyRepository;

    @Autowired
    CandidateRepository candidateRepository;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    VotesRepository votesRepository;

    @GetMapping("/parties")
    public ResponseEntity<?> getAllParties() {
        return ResponseEntity.ok(partyRepository.findAll());
    }

    @GetMapping("/party/{partyName}")
    public ResponseEntity<?> getParty(@PathVariable String partyName) {
        Optional<Party> getParty = partyRepository.findByName(partyName);

        if (getParty.isPresent()) {
            return ResponseEntity.ok(getParty.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Party not found with name: " + partyName));
        }
    }

    @GetMapping("/candidate/{candidateName}")
    public ResponseEntity<?> getPartyCandidate(@PathVariable String candidateName) {
        Optional<Candidate> getCandidate = candidateRepository.findByLastName(candidateName);

        if (getCandidate.isPresent()) {
            return ResponseEntity.ok(getCandidate.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Party not found with name: " + candidateName));
        }
    }
}
