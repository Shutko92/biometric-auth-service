package com.pm.biometric_auth_service.repository;

import com.pm.biometric_auth_service.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {
    List<Device> findByAccountId(Integer accountId);
}
