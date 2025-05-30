package com.pm.biometric_auth_service.repositories;

import com.pm.biometric_auth_service.models.BiometricSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BiometricSettingsRepository extends JpaRepository<BiometricSettings, Integer> {

    @Query("select bs from BiometricSettings bs join fetch bs.devices where bs.userId = :userId")
    Optional<BiometricSettings> findByUserId(int userId);
}
