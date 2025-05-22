package com.noyex.service.service;

import com.noyex.data.model.cart.CartSession;

import java.math.BigDecimal;

public interface ICartService {
    CartSession getOrCreateCart(String cookieId);
    void addMenuItemToCart(String cookieId, Long menuItemId);
    void updateCartMenuItemQuantity(String cookieId, Long menuItemId, int quantity);
    void removeMenuItemFromCart(String cookieId, Long menuItemId);
    void clearCart(String cookieId);
    BigDecimal getTotalPrice(String cookieId);
    int getTotalItems(String cookieId);
}
