package com.noyex.service.client.geocoder;

import com.noyex.data.dtos.OrderDto;
import com.noyex.service.client.exception.FailedToFetchAddressForCoordinatesException;
import com.noyex.service.client.geocoder.contract.CoordinatesDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CoordinatesClient implements ICoordinatesClient {

    RestTemplate restTemplate;
    ICoordinatesClientUriBuilderProvider uriBuilderProvider;

    public CoordinatesClient(ICoordinatesClientUriBuilderProvider uriBuilderProvider) {
        this.uriBuilderProvider = uriBuilderProvider;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public CoordinatesDto fetchCoordinatesByAddress(OrderDto orderDto) {
        String url = uriBuilderProvider.builder()
                .pathSegment("search")
                .queryParam("city", orderDto.getCity())
                .queryParam("street", orderDto.getAddress())
                .queryParam("country", "PL")
                .build()
                .toUriString();

        CoordinatesDto[] response = restTemplate.getForObject(url, CoordinatesDto[].class);
        if (response == null) {
            throw new FailedToFetchAddressForCoordinatesException("Failed to fetch coordinates for address: " + orderDto.getAddress() + ", " + orderDto.getZipCode() + ", " + orderDto.getCity());
        }
        return response[0];
    }
}
