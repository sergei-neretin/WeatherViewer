package com.sergeineretin.weatherviewer.exceptions;

public class SessionCookieNotFoundException extends RuntimeException {
    public SessionCookieNotFoundException(String message) {
        super(message);
    }
}
