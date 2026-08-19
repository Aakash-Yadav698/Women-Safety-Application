package com.womensafety.service;

import com.womensafety.dto.SosRequest;
import com.womensafety.dto.SosResponseDTO;
import com.womensafety.entity.EmergencyContact;
import com.womensafety.entity.SosAlert;
import com.womensafety.entity.User;
import com.womensafety.repository.EmergencyContactRepository;
import com.womensafety.repository.SosAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SosAlertService {

    private final SosAlertRepository sosAlertRepository;
    private final EmergencyContactRepository contactRepository;
    private final UserService userService;
    private final GeocodingService geocodingService;
    private final SmsService smsService;

    public SosResponseDTO triggerSos(Long userId, SosRequest request) {
        User user = userService.findUserOrThrow(userId);

        // 1. Turn coordinates into a readable address (best-effort)
        String address = geocodingService.reverseGeocode(request.getLatitude(), request.getLongitude());

        // 2. Save the alert FIRST - the incident record must exist even if
        //    every SMS below fails to send.
        SosAlert alert = new SosAlert();
        alert.setLatitude(request.getLatitude());
        alert.setLongitude(request.getLongitude());
        alert.setAddress(address);
        alert.setUser(user);
        alert = sosAlertRepository.save(alert);

        // 3. Notify every emergency contact
        List<EmergencyContact> contacts = contactRepository.findByUserId(userId);
        String messageBody = buildAlertMessage(user, address, request.getLatitude(), request.getLongitude());

        for (EmergencyContact contact : contacts) {
            smsService.sendSosAlert(contact.getPhoneNumber(), messageBody);
        }

        alert.setContactsNotified(true);
        sosAlertRepository.save(alert);

        return toDTO(alert, contacts.size());
    }

    public List<SosResponseDTO> getHistory(Long userId) {
        return sosAlertRepository.findByUserIdOrderByTriggeredAtDesc(userId).stream()
                .map(a -> toDTO(a, contactRepository.findByUserId(userId).size()))
                .collect(Collectors.toList());
    }

    private String buildAlertMessage(User user, String address, Double lat, Double lng) {
        String mapsLink = "https://maps.google.com/?q=" + lat + "," + lng;
        return user.getFullName() + " has triggered an SOS alert!\n"
                + "Location: " + address + "\n"
                + "Map: " + mapsLink;
    }

    private SosResponseDTO toDTO(SosAlert alert, int contactsCount) {
        return new SosResponseDTO(
                alert.getId(),
                alert.getLatitude(),
                alert.getLongitude(),
                alert.getAddress(),
                alert.getTriggeredAt(),
                alert.getContactsNotified(),
                contactsCount
        );
    }
}
