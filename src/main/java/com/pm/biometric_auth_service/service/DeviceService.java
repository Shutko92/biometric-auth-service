package com.pm.biometric_auth_service.service;

import com.pm.biometric_auth_service.model.Device;
import com.pm.biometric_auth_service.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceService {
    private final DeviceRepository deviceRepository;

    List<Device> findByAccountId(Integer accountId) {
        return deviceRepository.findByAccountId(accountId);
    }
}
