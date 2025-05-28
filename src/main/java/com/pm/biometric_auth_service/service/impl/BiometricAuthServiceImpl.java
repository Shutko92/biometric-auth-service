package com.pm.biometric_auth_service.service.impl;

import com.pm.biometric_auth_service.dto.BiometricAuthRequest;
import com.pm.biometric_auth_service.dto.BiometricSettingsResponse;
import com.pm.biometric_auth_service.exception.IllegalAuthStateException;
import com.pm.biometric_auth_service.mapper.BiometricSettingsMapper;
import com.pm.biometric_auth_service.model.BiometricSettings;
import com.pm.biometric_auth_service.repository.BiometricSettingsRepository;
import com.pm.biometric_auth_service.service.BiometricAuthService;
import com.pm.biometric_auth_service.service.LoginManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BiometricAuthServiceImpl implements BiometricAuthService {
    private final BiometricSettingsRepository settingsRepository;
    private final LoginManager loginManager;

    @Override
    public BiometricSettingsResponse enableBiometricAuth(BiometricAuthRequest request) {
        BiometricSettings settings = settingsRepository.save(BiometricSettings.builder()
                .biometricEnabled(true)
                .deviceInfo(request.deviceInfo())
                .userId(request.userId()).build());
        return BiometricSettingsMapper.toDto(settings);
    }

    @Override
    public String getBiometricAuthStatus(Integer userId) {
        BiometricSettings settings = settingsRepository.findByUserId(userId).orElseThrow(() ->
                new IllegalAuthStateException("User with Id " + userId + " not found"));
        String status = "";
        if (settings.getBiometricEnabled()) {
            status = "Active";
        } else {
            status = "Inactive";
        }

        return String.format("Status of the user with Id %d is: %s", userId, status);
    }

    @Override
    public BiometricSettingsResponse biometricAuthLogin(Integer userId) {
        BiometricSettings settings = settingsRepository.findByUserId(userId).orElseThrow(() ->
                new IllegalAuthStateException("User with Id " + userId + " not found"));

        return null;
    }
}
