package com.noyex.client;

import com.noyex.client.geocoder.CoordinatesClient;
import com.noyex.client.geocoder.CoordinatesClientUriBuilderProvider;
import com.noyex.client.geocoder.ICoordinatesClient;
import com.noyex.client.geocoder.ICoordinatesClientUriBuilderProvider;
import com.noyex.client.geocoder.contract.CoordinatesDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
//
//        ICoordinatesClient coordinatesClient = new CoordinatesClient(new CoordinatesClientUriBuilderProvider("67ed6a1d8ff8d917874562ejgeea8a7", "geocode.maps.co"));
//
//        CoordinatesDto coordinates = coordinatesClient.fetchCoordinatesByPostalCode("81-124");
//        System.out.println("Coordinates: " + coordinates.getLat() + ", " + coordinates.getLon());
    }
}
