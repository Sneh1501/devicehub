package com.global1.webservice.devicehub.deviceapi;

import com.global1.webservice.devicehub.deviceapi.model.Device;
import com.global1.webservice.devicehub.deviceapi.model.DeviceDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceMapper mapper;

    public DeviceService(DeviceRepository deviceRepository, DeviceMapper mapper) {
        this.deviceRepository = deviceRepository;
        this.mapper = mapper;
    }

    public DeviceDTO createDevice(DeviceDTO deviceDto) {
        Device device = mapper.toEntity(deviceDto);
        device.setCreationTime(LocalDateTime.now());
        return mapper.toDTO(deviceRepository.save(device));
    }
}
