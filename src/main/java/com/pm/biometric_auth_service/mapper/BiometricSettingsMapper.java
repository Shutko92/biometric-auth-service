package com.pm.biometric_auth_service.mapper;

import com.pm.biometric_auth_service.dto.BiometricSettingsResponse;
import com.pm.biometric_auth_service.model.BiometricSettings;

public class BiometricSettingsMapper {
    public static BiometricSettingsResponse toDto(BiometricSettings settings) {
        BiometricSettingsResponse response = new BiometricSettingsResponse();
        response.setId(settings.getId());
        response.setBiometricEnabled(settings.getBiometricEnabled());
        response.setUserId(settings.getUserId());
        response.setLastUsed(settings.getLastUsed());
        response.setDeviceInfo(settings.getDeviceInfo());
        return response;
    }
}
