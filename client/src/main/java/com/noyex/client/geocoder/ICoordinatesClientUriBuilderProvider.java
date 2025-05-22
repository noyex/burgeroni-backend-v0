package com.noyex.client.geocoder;

import org.springframework.web.util.UriComponentsBuilder;

public interface ICoordinatesClientUriBuilderProvider {
    String apiKey();
    String host();

    default UriComponentsBuilder builder() {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(host())
                .queryParam("api_key", apiKey());
    }
}
