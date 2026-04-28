package com.vt.atp.exception;

public class WrapperException extends RuntimeException {
    public WrapperException(String message) {
        super(message);
    }
    public WrapperException(String message, Throwable cause) {
        super(message,  cause);
    }
}
