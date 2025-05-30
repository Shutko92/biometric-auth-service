package com.pm.biometric_auth_service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
@Setter
@Getter
@ToString
public class BiometricSettingsResponse {
    Integer id;
    Integer userId;
    Boolean biometricEnabled;
    LocalDateTime lastUsed;
    String deviceInfo;
}

