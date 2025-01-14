package com.election.backendjava.models.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateId implements Serializable {

    @Column(name = "candidate_id")
    private int candidateId;

    @Column(name = "party_id")
    private int partyId;

}
