package com.global1.webservice.devicehub.deviceapi;

import com.global1.webservice.devicehub.deviceapi.model.Device;
import com.global1.webservice.devicehub.deviceapi.model.DeviceDTO;
import com.global1.webservice.devicehub.deviceapi.model.DeviceState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class DeviceServiceTest {
    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private DeviceMapper deviceMapper;

    @InjectMocks
    private DeviceService deviceService;

    private Device deviceEntity;
    private DeviceDTO deviceDTO;

    @BeforeEach
    void setUp() {
        deviceEntity = new Device(1L, "Device1", "BrandA", DeviceState.AVAILABLE);
        deviceDTO = new DeviceDTO(1L, "Device1", "BrandA", DeviceState.AVAILABLE);
    }

    @Test
    void createDevice_savesAndReturnsDTO() {
        Mockito.when(deviceMapper.toEntity(any())).thenReturn(deviceEntity);
        Mockito.when(deviceMapper.toDTO(deviceEntity)).thenReturn(deviceDTO);
        Mockito.when(deviceRepository.save(deviceEntity)).thenReturn(deviceEntity);

        DeviceDTO result = deviceService.createDevice(deviceDTO);
        assertEquals(deviceDTO.getName(), result.getName());
        Mockito.verify(deviceRepository).save(deviceEntity);
    }

    @Test
    void getDeviceById_found() {
        Mockito.when(deviceRepository.findById(1L)).thenReturn(Optional.of(deviceEntity));
        Mockito.when(deviceMapper.toDTO(deviceEntity)).thenReturn(deviceDTO);

        DeviceDTO result = deviceService.getDeviceById(1L);
        assertNotNull(result);
        assertEquals(deviceDTO.getName(), result.getName());
    }

    @Test
    void getDeviceById_notFound_returnsNull() {
        Mockito.when(deviceRepository.findById(1L)).thenReturn(Optional.empty());
        DeviceDTO result = deviceService.getDeviceById(1L);
        assertNull(result);
    }
}
