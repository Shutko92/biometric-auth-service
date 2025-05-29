package com.pm.biometric_auth_service.validators;

import com.pm.biometric_auth_service.service.BiometricAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final BiometricAuthService biometricAuthService;

//    public void validate(NewUserDto newUserDto) {
//        List<String> errorMessages = new ArrayList<>();
//        Optional<User> user = userService.findByEmail(newUserDto.getEmail());
//        if (user.isPresent()) {
//            errorMessages.add(String.format("A user with email: %s is already exists.", newUserDto.getEmail()));
//        }
//        if (newUserDto.getUsername() == null || newUserDto.getUsername().isBlank()) {
//            errorMessages.add("The username field is not filled in.");
//        }
//        if (newUserDto.getPassword() == null || newUserDto.getPassword().isBlank()) {
//            errorMessages.add("The password field is not filled in.");
//        }
//        if (newUserDto.getEmail() == null || newUserDto.getEmail().isBlank()) {
//            errorMessages.add("The email field is not filled in.");
//        }
//        if (!errorMessages.isEmpty()) {
//            throw new ValidationException(errorMessages);
//        }
//    }
}
