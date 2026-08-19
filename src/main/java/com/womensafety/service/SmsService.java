package com.womensafety.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.from-number}")
    private String fromNumber;

    private boolean configured = false;

    // Runs once at startup, after Spring has injected the @Value fields
    @PostConstruct
    public void init() {
        if (isPlaceholder(accountSid) || isPlaceholder(authToken) || isPlaceholder(fromNumber)) {
            System.out.println("[SmsService] Twilio credentials not set - SOS SMS will be logged, not sent.");
            configured = false;
            return;
        }
        Twilio.init(accountSid, authToken);
        configured = true;
    }

    /**
     * Sends the alert SMS to one contact. Returns true if it was actually
     * sent, false if it was just logged (no creds) or failed.
     */
    public boolean sendSosAlert(String toPhoneNumber, String messageBody) {
        if (!configured) {
            System.out.println("[SmsService] (SIMULATED) To: " + toPhoneNumber + " | " + messageBody);
            return false;
        }

        try {
            Message.creator(
                    new PhoneNumber(toPhoneNumber),
                    new PhoneNumber(fromNumber),
                    messageBody
            ).create();
            return true;
        } catch (Exception e) {
            // On the Twilio free trial, unverified recipient numbers throw here -
            // we log and move on rather than failing the whole SOS request.
            System.out.println("[SmsService] Failed to send to " + toPhoneNumber + ": " + e.getMessage());
            return false;
        }
    }

    private boolean isPlaceholder(String value) {
        return value == null || value.isBlank() || value.startsWith("YOUR_");
    }
}
