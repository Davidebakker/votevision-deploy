package com.election.backendjava.models.user;

import com.election.backendjava.models.form.Comment;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId = null;

    @Column(name = "name")
    private String name;

    @NotBlank
    @Size(max = 32)
    private String username;

    @NotBlank
    @Email
    @Size(max = 64)
    private String email;

    @NotBlank
    @Size(min = 8, max = 120)
    private String password;

    @NotBlank
    @Column(name = "region")
    private String region;

    @Column(name = "banned")
    private Boolean banned = Boolean.FALSE;

    @Column(name = "ban_expiration")
    private LocalDateTime banExpiration;

    @Column(name = "ban_count")
    private Integer banCount = 0;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(  name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();

    public User(String username, String email, String name, String region, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.region = region;
    }

}
