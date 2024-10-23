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
public class Party {

    @Id
    @Column(name = "party_id")
    private int partyId;

    private String name;

    private String logo;

    
    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL)
    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Candidate> candidates = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder candidatesList = new StringBuilder();
        if (candidates != null) {
            for (Candidate candidate : candidates) {
                candidatesList.append(candidate.toString()).append("\n");
            }
        }
        return "Party{" +
                "partyId=" + partyId +
                ", name='" + name + '\'' +
                ", candidates=\n" + candidatesList +
                '}';
    }
}
