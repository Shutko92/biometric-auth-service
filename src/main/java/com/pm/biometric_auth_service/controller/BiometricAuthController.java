package com.pm.biometric_auth_service.controller;

import com.pm.biometric_auth_service.dto.*;
import com.pm.biometric_auth_service.exception.AppError;
import com.pm.biometric_auth_service.mappers.BiometricSettingsMapper;
import com.pm.biometric_auth_service.services.impl.BiometricAuthServiceImpl;
import com.pm.biometric_auth_service.utils.JwtTokenUtil;
import com.pm.biometric_auth_service.validators.RegisterValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/biometric")
@RequiredArgsConstructor
public class BiometricAuthController {
    private final BiometricAuthServiceImpl biometricAuthService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final RegisterValidator registerValidator;
    private final BiometricSettingsMapper mapper;

    @PostMapping("/enable")
    public ResponseEntity<BiometricSettingsResponse> enableBiometricAuth(@RequestBody BiometricRegisterRequest request) {
        registerValidator.validate(request);
        return ResponseEntity.ok(mapper.getSettingsDto(biometricAuthService.enableBiometricAuth(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<BiometricAuthResponse> biometricAuthLogin(@RequestBody BiometricAuthRequest request) {
        return ResponseEntity.ok(biometricAuthService.biometricAuthLogin(request));
    }

    @GetMapping("/status")
    public ResponseEntity<String> getBiometricAuthStatus(@RequestParam Integer userId) {
        return ResponseEntity.ok(biometricAuthService.getBiometricAuthStatus(userId));
    }

    @PostMapping("/login2")
    public ResponseEntity<?> createAuthToken(@RequestBody BiometricAuthRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequest.userId(), authRequest.deviceInfo()));

        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(),
                    "Incorrect user id or device info."), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = biometricAuthService.loadUserByUsername(String.valueOf(authRequest.userId()));
        String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
