package com.pm.biometric_auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BiometricAuthResponse {
    private String content;
    private int status;
}
