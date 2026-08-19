package com.womensafety.controller;

import com.womensafety.dto.SosRequest;
import com.womensafety.dto.SosResponseDTO;
import com.womensafety.security.AuthUtil;
import com.womensafety.service.SosAlertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sos")
@RequiredArgsConstructor
public class SosController {

    private final SosAlertService sosAlertService;

    @PostMapping("/trigger")
    public ResponseEntity<SosResponseDTO> trigger(@Valid @RequestBody SosRequest request) {
        Long userId = AuthUtil.getCurrentUserId();
        SosResponseDTO alert = sosAlertService.triggerSos(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(alert);
    }

    @GetMapping("/history")
    public List<SosResponseDTO> history() {
        Long userId = AuthUtil.getCurrentUserId();
        return sosAlertService.getHistory(userId);
    }
}
