package com.election.backendjava.models.election;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Votes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer votesId;

    private int amount;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "partyId", referencedColumnName = "partyId", insertable = false, updatable = false),
            @JoinColumn(name = "candidateId", referencedColumnName = "candidateId", insertable = false, updatable = false)
    })
    @JsonBackReference
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "stationId", insertable = false, updatable = false)
    @JsonBackReference
    private Station station;

}

