package com.womensafety.repository;

import com.womensafety.entity.EmergencyContact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Long> {

    // SELECT * FROM emergency_contacts WHERE user_id = ?
    List<EmergencyContact> findByUserId(Long userId);

    // Used to make sure a user can only delete/edit THEIR OWN contact
    Optional<EmergencyContact> findByIdAndUserId(Long id, Long userId);
}
