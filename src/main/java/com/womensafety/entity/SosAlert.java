package com.womensafety.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "sos_alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SosAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    // Human-readable address filled in by GeocodingService.
    // Nullable because geocoding is optional/best-effort.
    @Column(length = 500)
    private String address;

    private LocalDateTime triggeredAt;

    // true once we've attempted to notify contacts (doesn't guarantee
    // delivery, just that the notify step ran)
    private Boolean contactsNotified = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @PrePersist
    protected void onCreate() {
        this.triggeredAt = LocalDateTime.now();
    }
}
