package com.election.backendjava.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PartyMunicipalityResultDTO {
    private String municipality;
    private String party;
    private Long votes;

    public PartyMunicipalityResultDTO(String municipality, String party, Long votes) {
        this.municipality = municipality;
        this.party = party;
        this.votes = votes;
    }

}

