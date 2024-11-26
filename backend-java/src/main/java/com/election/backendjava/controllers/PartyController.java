//package com.election.backendjava.controllers;
//
//import com.election.backendjava.models.election.Party;
//import com.election.backendjava.repositories.PartyRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/elections")
//@CrossOrigin(origins = "http://localhost:3001")
//public class PartyController {
//
//    @Autowired
//    private PartyRepository partyRepository;
//
//    // Endpoint om alle partijen op te halen
//    @GetMapping("/parties")
//    public List<Party> getAllParties() {
//        return partyRepository.findAll();
//    }
//}
