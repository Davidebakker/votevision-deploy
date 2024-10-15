package com.election.backendjava.controllers;

import com.election.backendjava.repositories.CandidateRepository;
import com.election.backendjava.repositories.PartyRepository;
import com.election.backendjava.repositories.StationRepository;
import com.election.backendjava.repositories.VotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/party")
    public ResponseEntity<?> getAllParties() {
        return ResponseEntity.ok(partyRepository.findAll());
    }
}
