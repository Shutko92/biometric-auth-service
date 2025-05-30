package com.pm.biometric_auth_service.dto;

import com.pm.biometric_auth_service.models.BiometricSettings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeviceDto {
    private Integer id;
    private Integer accountId;
    private String deviceInfo;
    private Boolean biometricEnabled;
}
