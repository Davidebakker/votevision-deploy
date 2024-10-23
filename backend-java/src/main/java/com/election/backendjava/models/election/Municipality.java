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
@Table(name = "municipalities")
public class Municipality {

    @Id
    @Column(name = "municipality_id")
    private String municipalityId;

    private String name;

//    @ManyToOne
//    @JoinColumn(name = "province_id", nullable = false)
//    @JsonBackReference
//    private Province province;

    @ManyToOne
    @JoinColumn(name = "constituency_id", nullable = false)
    @JsonBackReference
    private Constituency constituency;

    @OneToMany(mappedBy = "municipality", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Station> stations = new ArrayList<>();

    public void setMunicipalityId(String municipalityId) {
        int length = municipalityId.length();
        switch (length) {
            case 2:
                municipalityId = "00" + municipalityId;
                break;
            case 3:
                municipalityId = "0" + municipalityId;
                
                break;
            case 1:
                municipalityId = "000" + municipalityId;
                
                break;
        
            default:
                break;
        }
        this.municipalityId = municipalityId;
    }

}
