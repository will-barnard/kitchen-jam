package com.barnard.exception;

public class AuthException extends RuntimeException {
    public AuthException() {
        super();
    }
    public AuthException(String message) {
        super(message);
    }
    public AuthException(String message, Exception cause) {
        super(message, cause);
    }
}