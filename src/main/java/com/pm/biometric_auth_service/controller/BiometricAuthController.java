package com.pm.biometric_auth_service.controller;

import com.pm.biometric_auth_service.dto.*;

import com.pm.biometric_auth_service.service.BiometricAuthService;
import com.pm.biometric_auth_service.util.JwtTokenUtil;
import com.pm.biometric_auth_service.validator.RegisterValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/biometric")
@RequiredArgsConstructor
@Tag(name = "Biometric Authentication", description = "Endpoints for managing biometric authentication")
public class BiometricAuthController {
    private final BiometricAuthService biometricAuthService;
    private final JwtTokenUtil jwtTokenUtil;
    private final RegisterValidator registerValidator;

    @Operation(
            summary = "Enable biometric authentication",
            description = "Enables biometric authentication for a user after validation"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Biometric authentication enabled",
                    content = @Content(schema = @Schema(implementation = BiometricSettingsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/enable")
    public ResponseEntity<BiometricSettingsResponse> enableBiometricAuth(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Biometric registration request",
            required = true,
            content = @Content(schema = @Schema(implementation = BiometricRegisterRequest.class))) @RequestBody BiometricRegisterRequest request) {
        registerValidator.validate(request);
        return ResponseEntity.ok(biometricAuthService.enableBiometricAuth(request));
    }

    @Operation(
            summary = "Request OTP for biometric setup",
            description = "Generates a one-time password (OTP) for biometric authentication setup"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OTP generated successfully",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/request-otp")
    public ResponseEntity<String> requestBiometricAuth(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Biometric registration request",
            required = true,
            content = @Content(schema = @Schema(implementation = BiometricRegisterRequest.class)))@RequestBody BiometricRegisterRequest request) {
        return ResponseEntity.ok(biometricAuthService.requestBiometricAuth(request));
    }

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
    public ResponseEntity<?> biometricAuthLogin(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Biometric authentication credentials",
            required = true,
            content = @Content(schema = @Schema(implementation = BiometricAuthRequest.class))) @RequestBody BiometricAuthRequest request) {
        UserDetails userDetails = biometricAuthService.biometricAuthLogin(request);
        String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @Operation(
            summary = "Get biometric authentication status",
            description = "Retrieves the biometric authentication status for a user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Biometric authentication enabled",
                    content = @Content(schema = @Schema(implementation = BiometricSettingsResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/status")
    public ResponseEntity<BiometricSettingsResponse> getBiometricAuthStatus(@Parameter(
            name = "userId",
            description = "ID of the user to check status for",
            required = true,
            example = "12345"
    )@RequestParam Integer userId) {
        return ResponseEntity.ok(biometricAuthService.getBiometricAuthStatus(userId));
    }


}
