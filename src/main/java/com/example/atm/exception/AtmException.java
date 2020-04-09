package com.example.atm.exception;

public class AtmException extends RuntimeException {
    public AtmException(String message) {
        super(message);
    }

    public AtmException(String message, Throwable cause) {
        super(message, cause);
    }
}
