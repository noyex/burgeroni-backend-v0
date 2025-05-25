package com.noyex.service.client.geocoder;

import org.springframework.beans.factory.annotation.Value;

public record CoordinatesClientUriBuilderProvider(String apiKey, String host) implements ICoordinatesClientUriBuilderProvider {
    public CoordinatesClientUriBuilderProvider(
            @Value("${GEOCODER_API_KEY}")
            String apiKey,
            @Value("${GEOCODER_API_HOST}")
            String host) {
        this.apiKey = apiKey;
        this.host = host;
    }
}
