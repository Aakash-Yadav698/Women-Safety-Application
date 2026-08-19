package com.womensafety.repository;

import com.womensafety.entity.SosAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SosAlertRepository extends JpaRepository<SosAlert, Long> {

    // SELECT * FROM sos_alerts WHERE user_id = ? ORDER BY triggered_at DESC
    List<SosAlert> findByUserIdOrderByTriggeredAtDesc(Long userId);
}
