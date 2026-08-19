package com.womensafety.service;

import com.womensafety.dto.ContactRequest;
import com.womensafety.dto.ContactResponseDTO;
import com.womensafety.entity.EmergencyContact;
import com.womensafety.entity.User;
import com.womensafety.exception.ResourceNotFoundException;
import com.womensafety.repository.EmergencyContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmergencyContactService {

    private final EmergencyContactRepository contactRepository;
    private final UserService userService;

    public ContactResponseDTO addContact(Long userId, ContactRequest request) {
        User user = userService.findUserOrThrow(userId);

        EmergencyContact contact = new EmergencyContact();
        contact.setName(request.getName());
        contact.setPhoneNumber(request.getPhoneNumber());
        contact.setRelation(request.getRelation());
        contact.setUser(user);

        return toDTO(contactRepository.save(contact));
    }

    public List<ContactResponseDTO> getContacts(Long userId) {
        return contactRepository.findByUserId(userId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteContact(Long userId, Long contactId) {
        // findByIdAndUserId is the key line: it guarantees a user can only
        // delete THEIR OWN contact, not anyone else's by guessing an id.
        EmergencyContact contact = contactRepository.findByIdAndUserId(contactId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found"));
        contactRepository.delete(contact);
    }

    private ContactResponseDTO toDTO(EmergencyContact contact) {
        return new ContactResponseDTO(
                contact.getId(),
                contact.getName(),
                contact.getPhoneNumber(),
                contact.getRelation()
        );
    }
}
