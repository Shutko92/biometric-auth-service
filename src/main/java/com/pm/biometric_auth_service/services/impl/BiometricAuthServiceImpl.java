package com.pm.biometric_auth_service.services.impl;

import com.pm.biometric_auth_service.dto.BiometricAuthRequest;
import com.pm.biometric_auth_service.dto.BiometricAuthResponse;
import com.pm.biometric_auth_service.dto.BiometricRegisterRequest;
import com.pm.biometric_auth_service.exception.IllegalAuthStateException;
import com.pm.biometric_auth_service.exception.UserNotFoundException;
import com.pm.biometric_auth_service.models.BiometricSettings;
import com.pm.biometric_auth_service.models.Device;
import com.pm.biometric_auth_service.repositories.BiometricSettingsRepository;
import com.pm.biometric_auth_service.services.BiometricAuthService;
import com.pm.biometric_auth_service.services.DeviceService;
import com.pm.biometric_auth_service.services.LoginManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BiometricAuthServiceImpl implements BiometricAuthService, UserDetailsService {
    private final BiometricSettingsRepository settingsRepository;
    private final LoginManager loginManager;
    private final DeviceService deviceService;

    @Override
    public BiometricSettings enableBiometricAuth(BiometricRegisterRequest request) {
        Optional<BiometricSettings> settingsOPtional = findByUserId(request.userId());
        BiometricSettings settings = null;
        settings = settingsOPtional.orElseGet(() -> BiometricSettings.builder()
                .userId(request.userId())
                .devices(new ArrayList<>())
                .build());
        Device device = Device.builder()
                .account(settings)
                .deviceInfo(request.deviceInfo())
                .biometricEnabled(true)
                .build();
        settings.getDevices().add(device);
        return settingsRepository.save(settings);
    }

    @Override
    @Transactional
    public String getBiometricAuthStatus(Integer userId) {
//        BiometricSettings settings = settingsRepository.findByUserId(userId).orElseThrow(() ->
//                new IllegalAuthStateException("User with Id " + userId + " not found"));
//        String status;
//        if (settings.getBiometricEnabled()) {
//            status = "Active";
//        } else {
//            status = "Inactive";
//        }
//        settings.setLastUsed(null);
//        settingsRepository.save(settings);
//        return String.format("Status of the user with Id %d is: %s", userId, status);
        return null;
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
                username, null, getAuthority(List.of("ROLE_USER")));
    }

    public Collection<? extends GrantedAuthority> getAuthority(List<String> roles) {
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
