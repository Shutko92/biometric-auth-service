package com.pm.biometric_auth_service.exception;

import lombok.Data;

@Data
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
