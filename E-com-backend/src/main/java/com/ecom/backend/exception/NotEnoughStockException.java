package com.ecom.backend.exception;

public class NotEnoughStockException extends ProductNotFoundException {
    public NotEnoughStockException(String message) {
        super(message);
    }
}
