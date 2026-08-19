package com.womensafety.controller;

import com.womensafety.dto.ContactRequest;
import com.womensafety.dto.ContactResponseDTO;
import com.womensafety.security.AuthUtil;
import com.womensafety.service.EmergencyContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final EmergencyContactService contactService;

    @PostMapping
    public ResponseEntity<ContactResponseDTO> addContact(@Valid @RequestBody ContactRequest request) {
        Long userId = AuthUtil.getCurrentUserId();
        ContactResponseDTO created = contactService.addContact(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<ContactResponseDTO> getContacts() {
        Long userId = AuthUtil.getCurrentUserId();
        return contactService.getContacts(userId);
    }

    @DeleteMapping("/{contactId}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long contactId) {
        Long userId = AuthUtil.getCurrentUserId();
        contactService.deleteContact(userId, contactId);
        return ResponseEntity.noContent().build();
    }
}
