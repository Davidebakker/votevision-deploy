package com.election.backendjava.models.election;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ElectionData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String province;
    private String partyName;
    private Long validVotes;


    public ElectionData() {
    }


    public ElectionData(String province, String partyName, Long validVotes) {
        this.province = province;
        this.partyName = partyName;
        this.validVotes = validVotes;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public Long getValidVotes() {
        return validVotes;
    }

    public void setValidVotes(Long validVotes) {
        this.validVotes = validVotes;
    }
}
