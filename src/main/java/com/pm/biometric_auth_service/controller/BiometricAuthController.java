package com.pm.biometric_auth_service.controller;

import com.pm.biometric_auth_service.dto.BiometricAuthRequest;
import com.pm.biometric_auth_service.dto.BiometricSettingsResponse;
import com.pm.biometric_auth_service.service.BiometricAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/biometric")
@RequiredArgsConstructor
public class BiometricAuthController {
    private final BiometricAuthService biometricAuthService;

    @PostMapping("/enable")
    public ResponseEntity<BiometricSettingsResponse> enableBiometricAuth(@RequestBody BiometricAuthRequest request) {
        return ResponseEntity.ok(biometricAuthService.enableBiometricAuth(request));
    }

    @PostMapping("/login")
    public ResponseEntity<BiometricSettingsResponse> biometricAuthLogin(@RequestParam Integer userId) {
        return ResponseEntity.ok(biometricAuthService.biometricAuthLogin(userId));
    }

    @GetMapping("/status")
    public ResponseEntity<String> getBiometricAuthStatus(@RequestParam Integer userId) {
        return ResponseEntity.ok(biometricAuthService.getBiometricAuthStatus(userId));
    }
}
