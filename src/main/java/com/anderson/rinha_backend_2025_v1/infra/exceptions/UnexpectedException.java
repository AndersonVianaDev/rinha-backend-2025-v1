package com.anderson.rinha_backend_2025_v1.infra.exceptions;

public class UnexpectedException extends RuntimeException {
    public UnexpectedException(String message) {
        super(message);
    }
}
