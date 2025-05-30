package com.pm.biometric_auth_service.mappers;

import com.pm.biometric_auth_service.dto.BiometricSettingsResponse;
import com.pm.biometric_auth_service.dto.DeviceDto;
import com.pm.biometric_auth_service.models.BiometricSettings;
import com.pm.biometric_auth_service.models.Device;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class BiometricSettingsMapper {

    public BiometricSettingsResponse getSettingsDto(BiometricSettings settings) {
        BiometricSettingsResponse response = new BiometricSettingsResponse();
        response.setId(settings.getId());
        response.setUserId(settings.getUserId());
        response.setLastUsed(settings.getLastUsed());
        response.setDevices(new ArrayList<>());
        for (Device device : settings.getDevices()) {
            response.getDevices().add(getDeviceDto(device));
        }
        return response;
    }

    public DeviceDto getDeviceDto(Device device) {
        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setId(device.getId());
        deviceDto.setAccountId(device.getAccount().getId());
        deviceDto.setDeviceInfo(device.getDeviceInfo());
        deviceDto.setBiometricEnabled(device.getBiometricEnabled());
        return deviceDto;
    }
}
