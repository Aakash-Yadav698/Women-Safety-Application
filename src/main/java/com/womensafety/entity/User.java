package com.womensafety.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    // unique=true -> MySQL enforces this at the DB level too, not just in Java
    @Column(nullable = false, unique = true)
    private String email;

    // Stores the BCrypt HASH, never the raw password. We use @JsonIgnore
    // as a second layer of defense so even a coding mistake can't leak it
    // in a response - but DTOs are the real reason it can never leak.
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    // Last known live location - updated whenever the app reports location
    private Double latitude;
    private Double longitude;

    private LocalDateTime createdAt;

    // One user -> many emergency contacts
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmergencyContact> emergencyContacts = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
