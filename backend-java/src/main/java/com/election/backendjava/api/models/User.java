package com.election.backendjava.api.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table
@Builder
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String username;
    private String email;
    private String password;
    private String region;


}
