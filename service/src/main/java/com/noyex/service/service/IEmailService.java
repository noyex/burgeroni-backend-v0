package com.noyex.service.service;

import com.noyex.data.model.order.Orders;

public interface IEmailService {
    void sendOrderConfirmation(Orders orders);
    void sendUserRegistrationConfirmation(String email, String username);
}
