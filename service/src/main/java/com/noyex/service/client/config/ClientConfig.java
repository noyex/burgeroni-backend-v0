package com.noyex.service.client.config;

import com.noyex.service.client.geocoder.CoordinatesClient;
import com.noyex.service.client.geocoder.CoordinatesClientUriBuilderProvider;
import com.noyex.service.client.geocoder.ICoordinatesClientUriBuilderProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

    @Bean
    public ICoordinatesClientUriBuilderProvider coordinatesClientUriBuilderProvider(
            @Value("${GEOCODER_API_KEY}") String apiKey,
            @Value("${GEOCODER_API_HOST}") String host) {
        return new CoordinatesClientUriBuilderProvider(apiKey, host);
    }

    @Bean
    public CoordinatesClient coordinatesClient(ICoordinatesClientUriBuilderProvider uriBuilderProvider) {
        return new CoordinatesClient(uriBuilderProvider);
    }
}
