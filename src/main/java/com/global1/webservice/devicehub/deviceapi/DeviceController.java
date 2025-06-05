package com.global1.webservice.devicehub.deviceapi;

import com.global1.webservice.devicehub.deviceapi.model.DeviceDTO;
import com.global1.webservice.devicehub.deviceapi.model.DeviceState;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/devices")
public class DeviceController {
    @Autowired
    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    public ResponseEntity<DeviceDTO> createDevice(@Valid @RequestBody DeviceDTO deviceDTO) {
        //TODO: Update ResponseEntity to be more specific with customised ExceptionHandling
        return ResponseEntity.ok(deviceService.createDevice(deviceDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceDTO> getDevice(@PathVariable Long id) {
        DeviceDTO dto = deviceService.getDeviceById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceDTO> replaceDevice(
            @PathVariable Long id,
            @Valid @RequestBody DeviceDTO deviceDTO
    ) {
        DeviceDTO replaced = deviceService.updateDevice(id, deviceDTO);
        return replaced != null ? ResponseEntity.ok(replaced) : ResponseEntity.notFound().build();
    }

    //TODO: add documentation for URLs
    @GetMapping
    public ResponseEntity<List<DeviceDTO>> getDevicesByFilters(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) DeviceState state) {

        List<DeviceDTO> filteredDevices = deviceService.getDevicesByFilters(name, brand, state);

        return filteredDevices.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(filteredDevices);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDevice(@PathVariable Long id) {
        try {
            deviceService.deleteDeviceById(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
