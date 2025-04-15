package com.example.trainticket.exception;

public class UserAlreadyBookedException extends RuntimeException {
    public UserAlreadyBookedException(String message) {
        super(message);
    }
}
