package com.election.backendjava.models.election;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stations")
public class Station {
    @Id
    @Column(name = "station_id")
    private String stationId;
    private String name;
    private String zipcode;

    @ManyToOne
    @JoinColumn(name = "municipality_id", nullable = false)
    @JsonBackReference
    private Municipality municipality;
    

    @OneToMany(mappedBy = "station", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Votes> votes = new ArrayList<>();

}
