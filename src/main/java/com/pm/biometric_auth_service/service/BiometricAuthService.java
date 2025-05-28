package com.pm.biometric_auth_service.service;

import com.pm.biometric_auth_service.dto.BiometricAuthRequest;
import com.pm.biometric_auth_service.dto.BiometricSettingsResponse;

public interface BiometricAuthService {

    BiometricSettingsResponse enableBiometricAuth(BiometricAuthRequest request);

    String getBiometricAuthStatus(Integer userId);

    BiometricSettingsResponse biometricAuthLogin(Integer userId);
}
