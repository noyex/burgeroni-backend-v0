package com.noyex.service.exceptions;

public class CartSessionNotFoundException extends RuntimeException {
    public CartSessionNotFoundException(String message) {
        super(message);
    }
}
