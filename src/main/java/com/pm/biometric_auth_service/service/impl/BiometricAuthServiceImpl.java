package com.pm.biometric_auth_service.service.impl;

import com.pm.biometric_auth_service.dto.BiometricAuthRequest;
import com.pm.biometric_auth_service.dto.BiometricAuthResponse;
import com.pm.biometric_auth_service.dto.BiometricRegisterRequest;
import com.pm.biometric_auth_service.dto.BiometricSettingsResponse;
import com.pm.biometric_auth_service.exception.IllegalAuthStateException;
import com.pm.biometric_auth_service.exception.UserNotFoundException;
import com.pm.biometric_auth_service.mapper.BiometricSettingsMapper;
import com.pm.biometric_auth_service.model.BiometricSettings;
import com.pm.biometric_auth_service.repository.BiometricSettingsRepository;
import com.pm.biometric_auth_service.service.BiometricAuthService;
import com.pm.biometric_auth_service.service.LoginManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BiometricAuthServiceImpl implements BiometricAuthService, UserDetailsService {
    private final BiometricSettingsRepository settingsRepository;
    private final LoginManager loginManager;

    @Override
    public BiometricSettingsResponse enableBiometricAuth(BiometricRegisterRequest request) {
        BiometricSettings settings = settingsRepository.save(BiometricSettings.builder()
                .biometricEnabled(true)
                .deviceInfo(request.deviceInfo())
                .userId(request.userId()).build());
        return BiometricSettingsMapper.toDto(settings);
    }

    @Override
    @Transactional
    public String getBiometricAuthStatus(Integer userId) {
        BiometricSettings settings = settingsRepository.findByUserId(userId).orElseThrow(() ->
                new IllegalAuthStateException("User with Id " + userId + " not found"));
        String status;
        if (settings.getBiometricEnabled()) {
            status = "Active";
        } else {
            status = "Inactive";
        }
        settings.setLastUsed(null);
        settingsRepository.save(settings);
        return String.format("Status of the user with Id %d is: %s", userId, status);
    }

    @Override
    public BiometricAuthResponse biometricAuthLogin(BiometricAuthRequest request) {

        if (loginManager.isBlocked(request.userId())) {
            return  new BiometricAuthResponse("Too many invalid requests. Try again later.", 429);
        }

        if (!request.authenticated()) {
            loginManager.incrementAttempts(request.userId());
            return  new BiometricAuthResponse("Failed login. Try again.", 429);
        }

        loginManager.resetAttempts(request.userId());
        BiometricSettings settings = settingsRepository.findByUserId(request.userId()).orElseThrow(() ->
                new IllegalAuthStateException("User with Id " + request + " not found"));
        settings.setLastUsed(null);
        settingsRepository.save(settings);
        return new BiometricAuthResponse("token", 200);
    }

    @Override
    public Optional<BiometricSettings> findByUserId(Integer userId) {
        return settingsRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        int userId = Integer.parseInt(username);
        BiometricSettings settings = findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User with Id " + userId + " not found"));
        return new org.springframework.security.core.userdetails.User(
                username, settings.getDeviceInfo(), getAuthority(List.of("ROLE_USER")));
    }

    public Collection<? extends GrantedAuthority> getAuthority(List<String> roles) {
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
