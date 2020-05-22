package com.example.atm.exception;

public class AuthenticateException extends RuntimeException {
    public AuthenticateException() {
    }

    public AuthenticateException(String message) {
        super(message);
    }

    public AuthenticateException(String message, Throwable cause) {
        super(message, cause);
    }
}
