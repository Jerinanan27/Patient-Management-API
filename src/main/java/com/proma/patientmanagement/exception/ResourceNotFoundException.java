package com.proma.patientmanagement.exception;

/**
 * Thrown when a requested patient does not exist. Mapped to HTTP 404
 * by the global exception handler.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
