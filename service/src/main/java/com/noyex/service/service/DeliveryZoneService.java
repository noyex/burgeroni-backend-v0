package com.noyex.service.service;

import com.noyex.data.dtos.OrderDto;
import com.noyex.data.dtos.PostalCodeDto;
import com.noyex.service.client.geocoder.ICoordinatesClient;
import com.noyex.service.client.geocoder.contract.CoordinatesDto;
import org.springframework.stereotype.Service;
import org.locationtech.jts.geom.*;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeliveryZoneService implements IDeliveryZoneService {

    private Polygon deliveryPolygon;
    private final ICoordinatesClient coordinatesClient;

    private static final Coordinate[] DELIVERY_AREA = {
            new Coordinate(18.5376378, 54.5874548),
            new Coordinate(18.4536096, 54.5787999),
            new Coordinate(18.4630509, 54.557951),
            new Coordinate(18.5584088, 54.5349501),
            new Coordinate(18.5590096, 54.5720339),
            new Coordinate(18.5376378, 54.5874548)
    };

    public DeliveryZoneService(ICoordinatesClient coordinatesClient) {
        this.coordinatesClient = coordinatesClient;
        GeometryFactory factory = new GeometryFactory();
        LinearRing ring = factory.createLinearRing(DELIVERY_AREA);
        this.deliveryPolygon = factory.createPolygon(ring, null);
    }

    @Override
    public boolean isInDeliveryZone(OrderDto orderDto) {
        CoordinatesDto coordinatesDto = coordinatesClient.fetchCoordinatesByAddress(orderDto);

        double lon = coordinatesDto.getLon();
        double lat = coordinatesDto.getLat();

        GeometryFactory factory = new GeometryFactory();
        Point point = factory.createPoint(new Coordinate(lon, lat));
        return deliveryPolygon.contains(point);
    }

}
