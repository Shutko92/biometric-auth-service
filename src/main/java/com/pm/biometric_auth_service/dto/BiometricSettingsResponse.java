package com.pm.biometric_auth_service.dto;

import lombok.Setter;

import java.time.LocalDateTime;
@Setter
public class BiometricSettingsResponse {
    Integer id;
    Integer userId;
    Boolean biometricEnabled;
    LocalDateTime lastUsed;
    String deviceInfo;
}

