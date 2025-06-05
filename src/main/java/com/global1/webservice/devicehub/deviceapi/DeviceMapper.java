package com.global1.webservice.devicehub.deviceapi;

import com.global1.webservice.devicehub.deviceapi.model.Device;
import com.global1.webservice.devicehub.deviceapi.model.DeviceDTO;
import org.springframework.stereotype.Component;

@Component
public class DeviceMapper {
    public Device toEntity(DeviceDTO dto) {
        if (dto == null) return null;

        Device device = new Device();
        device.setId(dto.getId());
        device.setName(dto.getName());
        device.setBrand(dto.getBrand());
        device.setState(dto.getState()); // Assumes enum values match
        return device;
    }

    public DeviceDTO toDTO(Device device) {
        if (device == null) return null;

        DeviceDTO dto = new DeviceDTO();
        dto.setId(device.getId());
        dto.setName(device.getName());
        dto.setBrand(device.getBrand());
        dto.setState(device.getState());
        dto.setCreationTime(device.getCreationTime());
        return dto;
    }
}
