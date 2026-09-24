package com.proma.patientmanagement.exception;

/**
 * Thrown when creating a patient whose email is already registered.
 * Mapped to HTTP 409 by the global exception handler.
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
