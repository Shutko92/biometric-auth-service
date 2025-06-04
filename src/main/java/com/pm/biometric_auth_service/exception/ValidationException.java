package com.pm.biometric_auth_service.exception;

import lombok.Data;

import java.util.List;

@Data
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
