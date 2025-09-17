package com.ionres.respondph.exception;

/**
 * Base exception for all domain-specific business rule violations.
 * Extend this in feature modules like admin, beneficiary, academic, etc.
 */
public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }

    public DomainException(Throwable cause) {
        super(cause);
    }
}

