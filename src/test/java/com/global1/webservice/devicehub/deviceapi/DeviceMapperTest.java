package com.global1.webservice.devicehub.deviceapi;

import com.global1.webservice.devicehub.deviceapi.model.Device;
import com.global1.webservice.devicehub.deviceapi.model.DeviceDTO;
import com.global1.webservice.devicehub.deviceapi.model.DeviceState;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeviceMapperTest {
    private final DeviceMapper mapper = new DeviceMapper();

    @Test
    void testToDTO() {
        Device device = new Device();
        device.setId(1L);
        device.setName("Device1");
        device.setBrand("BrandA");
        device.setState(DeviceState.AVAILABLE);
        device.setCreationTime(LocalDateTime.now());

        DeviceDTO dto = mapper.toDTO(device);

        assertEquals(device.getId(), dto.getId());
        assertEquals(device.getName(), dto.getName());
        assertEquals(device.getBrand(), dto.getBrand());
        assertEquals(device.getState(), dto.getState());
    }

    @Test
    void testToEntity() {
        DeviceDTO dto = new DeviceDTO();
        dto.setId(1L);
        dto.setName("Device2");
        dto.setBrand("BrandB");
        dto.setState(DeviceState.IN_USE);

        Device device = mapper.toEntity(dto);

        assertEquals(dto.getId(), device.getId());
        assertEquals(dto.getName(), device.getName());
        assertEquals(dto.getBrand(), device.getBrand());
        assertEquals(dto.getState(), device.getState());
    }
}