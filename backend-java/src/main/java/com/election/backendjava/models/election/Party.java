package com.election.backendjava.models.election;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "parties")
public class Party {

    @Id
    @Column(name = "party_id")
    private int partyId;

    private String name;

    private String logo;

    private int seats;

    private String description;

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL)
    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Candidate> candidates = new ArrayList<>();

    public Party(long partyId, String name, String logo, int seats) {
        this.partyId = (int) partyId;
        this.name = name;
        this.logo = logo;
        this.seats = seats;
        this.description = "";
        this.candidates = new ArrayList<>();
    }

}

