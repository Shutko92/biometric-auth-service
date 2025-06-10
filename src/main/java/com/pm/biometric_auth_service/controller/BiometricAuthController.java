package com.pm.biometric_auth_service.controller;

import com.pm.biometric_auth_service.dto.*;
import com.pm.biometric_auth_service.exception.AppError;
import com.pm.biometric_auth_service.service.BiometricAuthService;
import com.pm.biometric_auth_service.util.JwtTokenUtil;
import com.pm.biometric_auth_service.validator.FieldsValidator;
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
@RequestMapping("/api/v1/auth/biometric")
@RequiredArgsConstructor
@Tag(name = "Biometric Authentication", description = "Endpoints for managing biometric authentication")
public class BiometricAuthController {
    private final BiometricAuthService biometricAuthService;
    private final JwtTokenUtil jwtTokenUtil;
    private final FieldsValidator fieldsValidator;

    /**
     * Enables biometric authentication after validating registration fields and OTP.
     *
     * @param request Biometric registration request containing user credentials and device info
     * @return ResponseEntity with biometric settings configuration
     *
     * @see io.swagger.v3.oas.annotations.parameters.RequestBody
     * @see org.springframework.web.bind.annotation.RequestBody
     */
    @Operation(
            summary = "Enable biometric authentication",
            description = "Enables biometric authentication for a user after validation"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Biometric authentication enabled",
                    content = @Content(schema = @Schema(implementation = BiometricSettingsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters",
                    content = @Content(schema = @Schema(implementation = AppError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = AppError.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = AppError.class)))
    })
    @PostMapping("/enable")
    public ResponseEntity<BiometricSettingsResponse> enableBiometricAuth(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Biometric registration request",
            required = true,
            content = @Content(schema = @Schema(implementation = BiometricRegisterRequest.class))) @RequestBody BiometricRegisterRequest request) {
        fieldsValidator.registerValidate(request);
        return ResponseEntity.ok(biometricAuthService.enableBiometricAuth(request));
    }

    /**
     * Initiates biometric registration by sending OTP to user's phone.
     *
     * @param request Biometric registration request containing phone number
     * @return ResponseEntity with generated OTP
     *
     * @see io.swagger.v3.oas.annotations.parameters.RequestBody
     * @see org.springframework.web.bind.annotation.RequestBody
     */
    @Operation(
            summary = "Request OTP for biometric setup",
            description = "Generates a one-time password (OTP) for biometric authentication setup"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OTP generated successfully",
                    content = @Content(schema = @Schema(implementation = OtpResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters",
                    content = @Content(schema = @Schema(implementation = AppError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = AppError.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = AppError.class)))
    })
    @PostMapping("/request-otp")
    public ResponseEntity<OtpResponse> requestBiometricAuth(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Biometric registration request",
            required = true,
            content = @Content(schema = @Schema(implementation = BiometricRegisterRequest.class)))@RequestBody BiometricRegisterRequest request) {
        fieldsValidator.registerValidate(request);
        String otp = biometricAuthService.requestBiometricAuth(request);
        return ResponseEntity.ok(new OtpResponse(otp));
    }

    /**
     * Authenticates user via biometric credentials and returns JWT token.
     *
     * @param request Biometric authentication credentials
     * @return ResponseEntity with JWT token
     *
     * @see io.swagger.v3.oas.annotations.parameters.RequestBody
     * @see org.springframework.web.bind.annotation.RequestBody
     */
    @Operation(
            summary = "Authenticate using biometrics",
            description = "Performs biometric authentication and returns a JWT token upon success"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authentication successful",
                    content = @Content(schema = @Schema(implementation = OtpResponse.class))),
            @ApiResponse(responseCode = "401", description = "Biometric authentication failed",
                    content = @Content(schema = @Schema(implementation = AppError.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = AppError.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<?> biometricAuthLogin(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Biometric authentication credentials",
            required = true,
            content = @Content(schema = @Schema(implementation = BiometricAuthRequest.class))) @RequestBody BiometricAuthRequest request) {
        fieldsValidator.authValidate(request);
        UserDetails userDetails = biometricAuthService.biometricAuthLogin(request);
        String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    /**
     * Retrieves biometric authentication status for a specific user.
     *
     * @param userId ID of the user to check status for
     * @return ResponseEntity with biometric settings status (HTTP 200)
     *
     * @see io.swagger.v3.oas.annotations.Parameter
     * @see org.springframework.web.bind.annotation.RequestParam
     */
    @Operation(
            summary = "Get biometric authentication status",
            description = "Retrieves the biometric authentication status for a user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Biometric authentication enabled",
                    content = @Content(schema = @Schema(implementation = BiometricSettingsResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = AppError.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = AppError.class)))
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

    /**
     * Changes biometric enablement status for a specific device.
     *
     * @param request Device status change request containing enablement flag
     * @return ResponseEntity with updated device configuration (HTTP 200)
     *
     * @see io.swagger.v3.oas.annotations.parameters.RequestBody
     * @see org.springframework.web.bind.annotation.RequestBody
     */
    @Operation(
            summary = "Change device enabled status",
            description = "Enables/disables biometrics on a specific device"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Device description",
                    content = @Content(schema = @Schema(implementation = DeviceDto.class))),
            @ApiResponse(responseCode = "404", description = "User or device not found",
                    content = @Content(schema = @Schema(implementation = AppError.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = AppError.class)))
    })
    @PostMapping("device_enabled")
    public ResponseEntity<DeviceDto> changeDeviceEnableStatus(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Data on enabling biometrics on a specific device",
            required = true,
            content = @Content(schema = @Schema(implementation = DeviceStatusChangeRequest.class))) @RequestBody DeviceStatusChangeRequest request) {
        return ResponseEntity.ok(biometricAuthService.changeDeviceEnableStatus(request));
    }
}
