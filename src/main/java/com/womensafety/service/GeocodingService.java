package com.womensafety.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GeocodingService {

    @Value("${google.maps.api-key}")
    private String apiKey;

    // A plain new RestTemplate() is fine here since this is a single,
    // simple external call - no need to pull in Spring Cloud/WebClient for it.
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Turns coordinates into a human-readable address, e.g.
     * 26.8467, 80.9462 -> "Hazratganj, Lucknow, Uttar Pradesh, India".
     * Returns the raw "lat,long" string if the key is missing or the call fails -
     * geocoding is a nice-to-have, it must never block an SOS alert from saving.
     */
    @SuppressWarnings("unchecked")
    public String reverseGeocode(Double latitude, Double longitude) {
        String fallback = latitude + ", " + longitude;

        if (apiKey == null || apiKey.isBlank() || apiKey.equals("YOUR_GOOGLE_MAPS_API_KEY")) {
            return fallback;
        }

        try {
            String url = "https://maps.googleapis.com/maps/api/geocode/json"
                    + "?latlng=" + latitude + "," + longitude
                    + "&key=" + apiKey;

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response == null || !"OK".equals(response.get("status"))) {
                return fallback;
            }

            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
            if (results == null || results.isEmpty()) {
                return fallback;
            }

            return (String) results.get(0).get("formatted_address");
        } catch (Exception e) {
            // Never let a Maps API hiccup break the SOS flow
            return fallback;
        }
    }
}
