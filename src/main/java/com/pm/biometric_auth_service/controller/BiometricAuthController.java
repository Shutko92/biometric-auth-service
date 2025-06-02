package com.pm.biometric_auth_service.controller;

import com.pm.biometric_auth_service.dto.*;

import com.pm.biometric_auth_service.service.BiometricAuthService;
import com.pm.biometric_auth_service.util.JwtTokenUtil;
import com.pm.biometric_auth_service.validator.RegisterValidator;
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

    @Operation(
            summary = "Authenticate using biometrics",
            description = "Performs biometric authentication and returns a JWT token upon success"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authentication successful",
                    content = @Content(schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "401", description = "Biometric authentication failed"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/login")
    public ResponseEntity<?> biometricAuthLogin(@RequestBody BiometricAuthRequest request) {
        UserDetails userDetails = biometricAuthService.biometricAuthLogin(request);
        String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<String> getBiometricAuthStatus(@Parameter(
            name = "userId",
            description = "ID of the user to check status for",
            required = true,
            example = "12345"
    )@RequestParam Integer userId) {
        return ResponseEntity.ok(biometricAuthService.getBiometricAuthStatus(userId));
    }

//    @GetMapping("/status")
//    public ResponseEntity<BiometricSettingsResponse> getBiometricAuthStatus(@RequestParam Integer userId) {
//        return ResponseEntity.ok(biometricAuthService.getBiometricAuthStatus(userId));
//    }


}
