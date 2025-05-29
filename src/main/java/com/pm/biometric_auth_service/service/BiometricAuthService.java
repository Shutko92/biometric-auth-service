package com.pm.biometric_auth_service.service;

import com.pm.biometric_auth_service.dto.BiometricAuthRequest;
import com.pm.biometric_auth_service.dto.BiometricAuthResponse;
import com.pm.biometric_auth_service.dto.BiometricRegisterRequest;
import com.pm.biometric_auth_service.dto.BiometricSettingsResponse;

public interface BiometricAuthService {

    BiometricSettingsResponse enableBiometricAuth(BiometricRegisterRequest request);

    String getBiometricAuthStatus(Integer userId);

    BiometricAuthResponse biometricAuthLogin(BiometricAuthRequest request);
}
