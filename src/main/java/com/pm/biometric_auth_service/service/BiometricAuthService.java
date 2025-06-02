package com.pm.biometric_auth_service.service;

import com.pm.biometric_auth_service.dto.BiometricAuthRequest;
import com.pm.biometric_auth_service.dto.BiometricRegisterRequest;
import com.pm.biometric_auth_service.dto.BiometricSettingsResponse;
import com.pm.biometric_auth_service.dto.DeviceDto;
import com.pm.biometric_auth_service.model.BiometricSettings;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface BiometricAuthService {

    BiometricSettingsResponse enableBiometricAuth(BiometricRegisterRequest request);

    BiometricSettingsResponse getBiometricAuthStatus(Integer userId);

    UserDetails biometricAuthLogin(BiometricAuthRequest request);

    Optional<BiometricSettings> findByUserId(Integer userId);

    String requestBiometricAuth(BiometricRegisterRequest request);

    DeviceDto changeDeviceEnableStatus (BiometricAuthRequest request);
}
