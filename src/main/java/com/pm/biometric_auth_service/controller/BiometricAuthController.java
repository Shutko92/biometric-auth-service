package com.pm.biometric_auth_service.controller;

import com.pm.biometric_auth_service.dto.BiometricAuthRequest;
import com.pm.biometric_auth_service.dto.BiometricRegisterRequest;
import com.pm.biometric_auth_service.dto.BiometricSettingsResponse;
import com.pm.biometric_auth_service.dto.JwtResponse;

import com.pm.biometric_auth_service.services.BiometricAuthService;
import com.pm.biometric_auth_service.utils.JwtTokenUtil;
import com.pm.biometric_auth_service.validators.RegisterValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/biometric")
@RequiredArgsConstructor
public class BiometricAuthController {
    private final BiometricAuthService biometricAuthService;
    private final JwtTokenUtil jwtTokenUtil;
    private final RegisterValidator registerValidator;

    @PostMapping("/enable")
    public ResponseEntity<BiometricSettingsResponse> enableBiometricAuth(@RequestBody BiometricRegisterRequest request) {
        registerValidator.validate(request);
        return ResponseEntity.ok(biometricAuthService.enableBiometricAuth(request));
    }

    @PostMapping("/request-otp")
    public ResponseEntity<String> requestBiometricAuth(@RequestBody BiometricRegisterRequest request) {
        return ResponseEntity.ok(biometricAuthService.requestBiometricAuth(request));
    }

//    @PostMapping("/enable")
//    public ResponseEntity<BiometricSettingsResponse> enableBiometricAuth(@RequestBody BiometricRegisterRequest request) {
//        registerValidator.validate(request);
//        return ResponseEntity.ok(mapper.getSettingsDto(biometricAuthService.enableBiometricAuth(request)));
//    }

    @PostMapping("/login")
    public ResponseEntity<?> biometricAuthLogin(@RequestBody BiometricAuthRequest request) {
        UserDetails userDetails = biometricAuthService.biometricAuthLogin(request);
        String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @GetMapping("/status")
    public ResponseEntity<String> getBiometricAuthStatus(@RequestParam Integer userId) {
        return ResponseEntity.ok(biometricAuthService.getBiometricAuthStatus(userId));
    }
}
