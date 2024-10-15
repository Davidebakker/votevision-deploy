package com.election.backendjava.models.election;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "parties")
@NoArgsConstructor
@AllArgsConstructor
public class Party {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer partyId = null;

    private String name;
    private String logo;

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL)
    private List<Candidate> candidates = new ArrayList<>();
}
