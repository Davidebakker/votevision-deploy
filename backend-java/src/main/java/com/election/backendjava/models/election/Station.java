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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stations")
public class Station {
    @Id
    private String stationId;
    private String name;
    private String zipcode;

    @OneToMany(mappedBy = "station", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Votes> votes = new ArrayList<>();

    public Station(String stationId, String name, String zipcode) {
        this.stationId = stationId;
        this.name = name;
        this.zipcode = zipcode;
    }
}


