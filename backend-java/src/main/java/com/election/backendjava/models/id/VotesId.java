package com.election.backendjava.models.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VotesId implements Serializable {

    @Embedded
    private CandidateId candidateId;

    @Column(name = "station_id")
    private String stationId;

}

