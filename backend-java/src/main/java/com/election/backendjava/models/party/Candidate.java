package com.election.backendjava.models.party;

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
@Table(name = "parties")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;

    private String initials;

    private String firstName;

    private String lastName;

    private String locality;

    private Integer ranking;

    private String gender;

    private Integer amountOfVotes;

    private Boolean elected;

    @OneToMany
    @JoinTable(name = "candidate_party",
            joinColumns = @JoinColumn(name = "candidate_id"),
            inverseJoinColumns = @JoinColumn(name = "party_id"))
    private Set<Party> party = new HashSet<>();

}
