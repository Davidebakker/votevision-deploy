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

    @ManyToOne
    @JoinColumn(name = "municipality_id", nullable = false)
    private Municipality municipality;  // References `municipalities` table

    @ManyToOne
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;  // References `parties` table

    @ManyToOne
    @JoinColumn(name = "province_id", nullable = true)
    private Province province;  // References `provinces` table

    private Long votesReceived;

    private Integer electionYear;
}
