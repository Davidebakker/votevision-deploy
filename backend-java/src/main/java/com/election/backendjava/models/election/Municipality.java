package com.election.backendjava.models.election;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "municipalities")
public class Municipality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer municipalityId;

    private String name;

    @ManyToOne
    @JoinColumn(name = "constituencyId", nullable = false)
    @JsonBackReference
    private Constituency constituency;
}

