package com.noyex.service;


import com.noyex.service.service.DeliveryZoneService;
import com.noyex.service.service.IDeliveryZoneService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceApplication implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
//        ICoordinatesClient coordinatesClient = new CoordinatesClient(new CoordinatesClientUriBuilderProvider("67ed6a1d8ff8d917874562ejgeea8a7", "geocode.maps.co"));
//
//        IDeliveryZoneService deliveryZoneService = new DeliveryZoneService(coordinatesClient);
//        boolean isInZone = deliveryZoneService.isInDeliveryZone("81-124");
//        System.out.println("Is in delivery zone: " + isInZone);
    }

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }

}
