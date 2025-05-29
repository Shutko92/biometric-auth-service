package com.pm.biometric_auth_service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppError {
    private int statusCode;
    private String message;
}
