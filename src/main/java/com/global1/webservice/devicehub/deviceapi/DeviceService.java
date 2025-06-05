package com.global1.webservice.devicehub.deviceapi;

import com.global1.webservice.devicehub.deviceapi.model.Device;
import com.global1.webservice.devicehub.deviceapi.model.DeviceDTO;
import com.global1.webservice.devicehub.deviceapi.model.DeviceState;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

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

    public DeviceDTO getDeviceById(Long id) {
        return deviceRepository.findById(id)
                .map(mapper::toDTO)
                .orElse(null);
    }

    public DeviceDTO updateDevice(Long id, DeviceDTO updatedDto) {
        return deviceRepository.findById(id)
                .map(exsitingDevice -> {
                    // Check if device is IN_USE and if name/brand are being changed
                    if (exsitingDevice.getState() == DeviceState.IN_USE) {
                        if (!Objects.equals(exsitingDevice.getName(), updatedDto.getName())) {
                            throw new IllegalArgumentException("Cannot update device name when state is IN_USE");
                        }
                        if (!Objects.equals(exsitingDevice.getBrand(), updatedDto.getBrand())) {
                            throw new IllegalArgumentException("Cannot update device brand when state is IN_USE");
                        }
                    }

                    exsitingDevice.setName(updatedDto.getName());
                    exsitingDevice.setBrand(updatedDto.getBrand());
                    exsitingDevice.setState(updatedDto.getState());
                    return mapper.toDTO(deviceRepository.save(exsitingDevice));
                })
                .orElse(null);
    }

    public List<DeviceDTO> getDevicesByFilters(String name, String brand, DeviceState state) {
        if (brand != null) return deviceRepository.findByBrand(brand).stream().map(mapper::toDTO).toList();
        if (state != null) return deviceRepository.findByState(state).stream().map(mapper::toDTO).toList();
        if (name != null) return deviceRepository.findByName(name).stream().map(mapper::toDTO).toList();
        return getAllDevices();
    }

    public List<DeviceDTO> getAllDevices() {
        return deviceRepository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    public void deleteDeviceById(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Device not found with id: " + id));

        if (device.getState() == DeviceState.IN_USE)
            throw new IllegalArgumentException("Cannot delete device that is currently IN_USE");

        deviceRepository.deleteById(id);
    }
}
