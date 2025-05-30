package com.pm.biometric_auth_service.validators;

import com.pm.biometric_auth_service.dto.BiometricRegisterRequest;
import com.pm.biometric_auth_service.exception.ValidationException;
import com.pm.biometric_auth_service.model.BiometricSettings;
import com.pm.biometric_auth_service.service.BiometricAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RegisterValidator {
    private final BiometricAuthService biometricAuthService;

    public void validate(BiometricRegisterRequest request) {
        List<String> errorMessages = new ArrayList<>();
        Optional<BiometricSettings> settings = biometricAuthService.findByUserIdAndDeviceInfo(request);
        if (settings.isPresent()) {
            errorMessages.add(String.format("Учётная запись для этого устройства и пользователя с id: %d уже существует", request.userId()));
        }
        if (request.userId() == null) {
            errorMessages.add("Нет информации о userId.");
        }
        if (request.deviceInfo() == null || request.deviceInfo().isBlank()) {
            errorMessages.add("Нет информации об устройстве.");
        }
        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }
}
