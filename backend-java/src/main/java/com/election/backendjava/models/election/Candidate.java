package com.election.backendjava.models.election;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "candidates")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer candidateId = null;

    private String firstName;
    private String lastNamePrefix;
    private String lastName;
    private String initials;
    private String locality;

    @ManyToOne
    @MapsId("partyId")
    @JoinColumn(name = "partyId", nullable = false)
    private Party party;
}
