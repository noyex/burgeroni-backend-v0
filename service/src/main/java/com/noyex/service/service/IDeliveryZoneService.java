package com.noyex.service.service;


import com.noyex.data.dtos.OrderDto;
import com.noyex.data.dtos.PostalCodeDto;

public interface IDeliveryZoneService {
    boolean isInDeliveryZone(OrderDto orderDto);
}
