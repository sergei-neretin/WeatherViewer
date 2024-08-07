package com.sergeineretin.weatherviewer.exceptions;

public class UniqueConstraintViolationException extends RuntimeException {
    public UniqueConstraintViolationException(String message) {
        super(message);
    }
}
