package com.election.backendjava.models.election;

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
@Table(name = "constituencies")
public class Constituency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer constituency = null;
    private String name;

    @OneToMany(mappedBy = "constituency", cascade = CascadeType.ALL)
    private List<Municipality> municipality = new ArrayList<>();

}


