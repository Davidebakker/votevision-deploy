package com.election.backendjava.models.election;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @Column(name = "last_name")
    private String lastName;
    private String initials;
    private String locality;

    @ManyToOne
    @MapsId("partyId")
    @JoinColumn(name = "partyId", nullable = false)
    @JsonBackReference
    private Party party;
}
