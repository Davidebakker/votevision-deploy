package com.election.backendjava.repositories;

import com.election.backendjava.models.election.ElectionData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectionRepository extends JpaRepository<ElectionData, Long> {
    ElectionData findByProvince(String provinceId);
//     static ElectionData getElectionDataByProvince(String provinceId);
}
