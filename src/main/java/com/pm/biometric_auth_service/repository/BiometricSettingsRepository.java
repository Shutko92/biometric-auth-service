package com.pm.biometric_auth_service.repository;

import com.pm.biometric_auth_service.model.BiometricSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BiometricSettingsRepository extends JpaRepository<BiometricSettings, Integer> {
    Optional<BiometricSettings> findByUserId(int userId);
}
