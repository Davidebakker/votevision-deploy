package com.election.backendjava.models.election;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "election_results")
public class ElectionResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resultId;

//    @ManyToOne
//    @JoinColumn(name = "province_id", nullable = false)
//    private Province province;

    @ManyToOne
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;

    private Long votesReceived;

    private Integer electionYear;
}
