package com.pm.biometric_auth_service.controller;

import com.pm.biometric_auth_service.dto.BiometricAuthRequest;
import com.pm.biometric_auth_service.dto.BiometricAuthResponse;
import com.pm.biometric_auth_service.dto.BiometricRegisterRequest;
import com.pm.biometric_auth_service.dto.BiometricSettingsResponse;
import com.pm.biometric_auth_service.exception.AppError;
import com.pm.biometric_auth_service.service.impl.BiometricAuthServiceImpl;
import com.pm.biometric_auth_service.utils.JwtTokenUtil;
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

    @PostMapping("/enable")
    public ResponseEntity<BiometricSettingsResponse> enableBiometricAuth(@RequestBody BiometricRegisterRequest request) {
        return ResponseEntity.ok(biometricAuthService.enableBiometricAuth(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> biometricAuthLogin(@RequestBody BiometricAuthRequest request) {
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
                    "Incorrect username or password."), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = biometricAuthService.loadUserByUsername(String.valueOf(authRequest.userId()));
        String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(token);
    }
}
