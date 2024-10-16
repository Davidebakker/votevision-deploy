package com.election.backendjava.models.election;

import com.election.backendjava.models.id.VotesId;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Votes {

    @EmbeddedId
    private VotesId votesId;

    private int amount;


    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "party_id", referencedColumnName = "party_id", insertable = false, updatable = false),
            @JoinColumn(name = "candidate_id", referencedColumnName = "candidate_id", insertable = false, updatable = false)
    })
    @JsonBackReference
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "station_id", referencedColumnName = "station_id", insertable = false, updatable = false)
    @JsonBackReference
    private Station station;

}
