package com.election.backendjava.models.election;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(unique = true, nullable = false)
    private String name;

    private String logo;

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Candidate> candidates = new ArrayList<>();
}
