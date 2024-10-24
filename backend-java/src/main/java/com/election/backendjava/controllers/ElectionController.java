package com.election.backendjava.controllers;

import com.election.backendjava.models.election.Candidate;
import com.election.backendjava.models.election.Party;
import com.election.backendjava.payload.response.MessageResponse;
import com.election.backendjava.repositories.election.CandidateRepository;
import com.election.backendjava.repositories.election.PartyRepository;
import com.election.backendjava.repositories.election.StationRepository;
import com.election.backendjava.repositories.election.VotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    // Endpoint om alle partijen op te halen
    @GetMapping("/parties")
    public List<Party> getAllParties() {
        return partyRepository.findAll();
    }

    @GetMapping("/party/{partyName}")
    public ResponseEntity<?> getParty(@PathVariable String partyName) {
        Optional<Party> getParty = partyRepository.findByNameIgnoreCase(partyName);

        if (getParty.isPresent()) {
            return ResponseEntity.ok(getParty.get());
        } else {
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

    @GetMapping("/votes/party/{partyName}")
    public ResponseEntity<?> getPartyVotes(@PathVariable String partyName) {
        Optional<Party> getParty = partyRepository.findByNameIgnoreCase(partyName);

        if (getParty.isPresent()) {
            Party party = getParty.get();
            Long getVotes = votesRepository.getTotalVotesByPartyId(party.getPartyId());
            return ResponseEntity.ok(getVotes);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Party not found with name: " + partyName));

        }
    }

    @GetMapping("/votes/candidate/{candidateName}")
    public ResponseEntity<?> getCandidateVotes(@PathVariable String candidateName) {
        Optional<Candidate> getCandidate = candidateRepository.findByLastNameIgnoreCase(candidateName);

        if (getCandidate.isPresent()) {
            Candidate candidate = getCandidate.get();
            Long getVotes = votesRepository.getTotalVotesByCandidateId(candidate.getCandidateId());
            return ResponseEntity.ok(getVotes);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("candidate not found with name: " + candidateName));
        }
    }


}
