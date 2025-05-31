package com.pm.biometric_auth_service.services;

import com.pm.biometric_auth_service.dto.BiometricAuthRequest;
import com.pm.biometric_auth_service.dto.BiometricAuthResponse;
import com.pm.biometric_auth_service.dto.BiometricRegisterRequest;
import com.pm.biometric_auth_service.dto.BiometricSettingsResponse;
import com.pm.biometric_auth_service.models.BiometricSettings;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface BiometricAuthService {

    BiometricSettings enableBiometricAuth(BiometricRegisterRequest request);

    String getBiometricAuthStatus(Integer userId);

    UserDetails biometricAuthLogin(BiometricAuthRequest request);

    Optional<BiometricSettings> findByUserId(Integer userId);
}
