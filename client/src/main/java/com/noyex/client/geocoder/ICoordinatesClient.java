package com.noyex.client.geocoder;

import com.noyex.client.geocoder.contract.CoordinatesDto;
import com.noyex.data.dtos.OrderDto;

public interface ICoordinatesClient {
    CoordinatesDto fetchCoordinatesByAddress(OrderDto orderDto);
}
