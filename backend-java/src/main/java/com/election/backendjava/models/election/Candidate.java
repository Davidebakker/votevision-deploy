package com.election.backendjava.models.election;

import com.election.backendjava.models.id.CandidateId;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Candidate {

    @EmbeddedId
    private CandidateId candidateId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name_prefix")
    private String lastNamePrefix;

    @Column(name = "last_name")
    private String lastName;
    private String initails;
    private String locality;

    @ManyToOne
    @MapsId("party_id")
    @JoinColumn(name = "party_id", nullable = false)
    @JsonBackReference
    private Party party;
}
