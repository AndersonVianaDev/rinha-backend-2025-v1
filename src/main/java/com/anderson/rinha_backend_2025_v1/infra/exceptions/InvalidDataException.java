package com.anderson.rinha_backend_2025_v1.infra.exceptions;

public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String message) {
        super(message);
    }
}
