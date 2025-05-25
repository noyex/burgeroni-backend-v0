package com.noyex.service.client.geocoder;

import com.noyex.data.dtos.OrderDto;
import com.noyex.service.client.geocoder.contract.CoordinatesDto;

public interface ICoordinatesClient {
    CoordinatesDto fetchCoordinatesByAddress(OrderDto orderDto);
}
