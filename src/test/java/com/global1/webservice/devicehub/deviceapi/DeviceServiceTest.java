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

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
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

    private Device deviceEntity1;
    private Device deviceEntity2;
    private DeviceDTO deviceDTO1;
    private DeviceDTO deviceDTO2;

    @BeforeEach
    void setUp() {
        deviceEntity1 = new Device(1L, "Device1", "BrandA", DeviceState.AVAILABLE, LocalDateTime.now());
        deviceEntity2 = new Device(2L, "Device2", "BrandB", DeviceState.IN_USE, LocalDateTime.now());
        deviceDTO1 = new DeviceDTO(1L, "Device1", "BrandA", DeviceState.AVAILABLE);
        deviceDTO2 = new DeviceDTO(2L, "Device2", "BrandB", DeviceState.IN_USE);
    }

    @Test
    void createDevice_savesAndReturnsDTO() {
        Mockito.when(deviceMapper.toEntity(any())).thenReturn(deviceEntity1);
        Mockito.when(deviceMapper.toDTO(deviceEntity1)).thenReturn(deviceDTO1);
        Mockito.when(deviceRepository.save(deviceEntity1)).thenReturn(deviceEntity1);

        DeviceDTO result = deviceService.createDevice(deviceDTO1);
        assertEquals(deviceDTO1.getName(), result.getName());
        Mockito.verify(deviceRepository).save(deviceEntity1);
    }

    @Test
    void getDeviceById_found() {
        Mockito.when(deviceRepository.findById(1L)).thenReturn(Optional.of(deviceEntity1));
        Mockito.when(deviceMapper.toDTO(deviceEntity1)).thenReturn(deviceDTO1);

        DeviceDTO result = deviceService.getDeviceById(1L);
        assertNotNull(result);
        assertEquals(deviceDTO1.getName(), result.getName());
    }

    @Test
    void getDeviceById_notFound_returnsException() {
        Mockito.when(deviceRepository.findById(1L)).thenReturn(Optional.empty());
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> deviceService.getDeviceById(1L)
        );

        assertEquals("Device not found with id: 1", exception.getMessage());
    }

    @Test
    void updateDevice_shouldUpdatesFields_returnsUpdatedDevice() {
        DeviceDTO updatedDto = new DeviceDTO(1L, "Device1", "BrandB", DeviceState.INACTIVE);
        Mockito.when(deviceRepository.findById(1L)).thenReturn(Optional.of(deviceEntity1));
        Mockito.when(deviceRepository.save(deviceEntity1)).thenReturn(deviceEntity1);
        Mockito.when(deviceMapper.toDTO(deviceEntity1)).thenReturn(updatedDto);

        DeviceDTO result = deviceService.updateDevice(1L, updatedDto);

        assertNotNull(result);
        assertEquals("BrandB", result.getBrand());
        assertEquals(DeviceState.INACTIVE, result.getState());
        Mockito.verify(deviceRepository).save(deviceEntity1);
    }

    @Test
    void updateDevice_whenInUseAndNameChanges_shouldThrow() {
        deviceEntity1.setState(DeviceState.IN_USE);
        DeviceDTO updatedDto = new DeviceDTO(1L, "NewName", "BrandA", DeviceState.IN_USE);

        Mockito.when(deviceRepository.findById(1L)).thenReturn(Optional.of(deviceEntity1));

        assertThrows(IllegalArgumentException.class,
                () -> deviceService.updateDevice(1L, updatedDto));
    }

    @Test
    void updateDevice_notFound_returnsException() {
        Mockito.when(deviceRepository.findById(1L)).thenReturn(Optional.empty());
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> deviceService.getDeviceById(1L)
        );

        assertEquals("Device not found with id: 1", exception.getMessage());
    }

    @Test
    void getDevicesByFilters_byBrandAndState() {
        Mockito.when(deviceRepository.findAll()).thenReturn(List.of(deviceEntity1, deviceEntity2));
        Mockito.when(deviceMapper.toDTO(deviceEntity1)).thenReturn(deviceDTO1);

        List<DeviceDTO> result = deviceService.getDevicesByFilters(null, "BrandA", DeviceState.AVAILABLE);

        assertEquals(1, result.size());
        assertEquals("BrandA", result.getFirst().getBrand());
        assertEquals("Device1", result.getFirst().getName());
        assertEquals(DeviceState.AVAILABLE, result.getFirst().getState());
    }

    @Test
    void getDevicesByFilters_byName() {
        Mockito.when(deviceRepository.findAll()).thenReturn(List.of(deviceEntity1, deviceEntity2));
        Mockito.when(deviceMapper.toDTO(deviceEntity1)).thenReturn(deviceDTO1);

        List<DeviceDTO> result = deviceService.getDevicesByFilters("Device1", null, null);

        assertEquals(1, result.size());
        assertEquals("Device1", result.getFirst().getName());
    }

    @Test
    void getDevicesByFilters_noFilters_returnsAll() {
        Mockito.when(deviceRepository.findAll()).thenReturn(List.of(deviceEntity1, deviceEntity1));
        Mockito.when(deviceMapper.toDTO(deviceEntity1)).thenReturn(deviceDTO1);

        List<DeviceDTO> result = deviceService.getDevicesByFilters(null, null, null);

        assertEquals(2, result.size());
        Mockito.verify(deviceRepository).findAll();
    }

    @Test
    void deleteDeviceById_whenNotInUse_shouldDelete() {
        Mockito.when(deviceRepository.findById(1L)).thenReturn(Optional.of(deviceEntity1));
        deviceService.deleteDeviceById(1L);
        Mockito.verify(deviceRepository).deleteById(1L);
    }

    @Test
    void deleteDeviceById_whenNotFound_shouldThrow() {
        Mockito.when(deviceRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> deviceService.deleteDeviceById(1L));
    }

    @Test
    void deleteDeviceById_whenInUse_shouldThrow() {
        Mockito.when(deviceRepository.findById(1L)).thenReturn(Optional.of(deviceEntity2));
        assertThrows(IllegalArgumentException.class, () -> deviceService.deleteDeviceById(1L));
    }

}
