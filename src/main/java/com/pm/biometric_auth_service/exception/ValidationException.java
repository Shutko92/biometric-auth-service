package com.pm.biometric_auth_service.exception;

import lombok.Data;

import java.util.List;

@Data
public class ValidationException extends RuntimeException {
    private List<String> errorFieldsMessages;

    public ValidationException(List<String> errorFieldsMessages) {
        super(String.join("; ", errorFieldsMessages));
        this.errorFieldsMessages = errorFieldsMessages;
    }
}
