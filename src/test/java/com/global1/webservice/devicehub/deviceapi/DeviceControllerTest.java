package com.global1.webservice.devicehub.deviceapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.global1.webservice.devicehub.deviceapi.model.DeviceDTO;
import com.global1.webservice.devicehub.deviceapi.model.DeviceState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.*;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DeviceController.class)
@AutoConfigureMockMvc
class DeviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DeviceService deviceService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private DeviceDTO validDevice;
    private DeviceDTO validDevice2;
    private DeviceDTO invalidDevice;

    @BeforeEach
    void setUp() {
        validDevice = new DeviceDTO(1L, "Device1", "BrandA", DeviceState.AVAILABLE);
        validDevice2 = new DeviceDTO(2L, "Device2", "BrandB", DeviceState.IN_USE);
        invalidDevice = new DeviceDTO(1L, " ", "Brand2", null); // invalid name, brand, and state
    }

    @Test
    void createDevice_shouldReturnDevice() throws Exception {
        when(deviceService.createDevice(any(DeviceDTO.class))).thenReturn(validDevice);

        mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDevice)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Device1"));
    }

    @Test
    void createDevice_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDevice)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void getDevice_shouldReturnDevice_whenFound() throws Exception {
        when(deviceService.getDeviceById(1L)).thenReturn(validDevice);

        mockMvc.perform(get("/devices/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Device1"));
    }

    @Test
    void getDevice_shouldReturnNotFound_whenNotFound() throws Exception {
        when(deviceService.getDeviceById(1L)).thenReturn(null);

        mockMvc.perform(get("/devices/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void replaceDevice_shouldReturnUpdatedDevice() throws Exception {
        when(deviceService.updateDevice(eq(1L), any(DeviceDTO.class))).thenReturn(validDevice);

        mockMvc.perform(put("/devices/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDevice)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("BrandA"));
    }

    @Test
    void replaceDevice_invalidInput_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/devices/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDevice)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void replaceDevice_notFound() throws Exception {
        when(deviceService.updateDevice(1L, validDevice)).thenReturn(null);

        mockMvc.perform(put("/devices/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDevice)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getDevicesByFilters_shouldReturnList() throws Exception {
        List<DeviceDTO> devices = List.of(validDevice);
        when(deviceService.getDevicesByFilters("Device1", null, null))
                .thenReturn(devices);

        mockMvc.perform(get("/devices?name=Device1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getDevicesByFilters_shouldReturnNoContent() throws Exception {
        when(deviceService.getDevicesByFilters("Invalid", null, null))
                .thenReturn(List.of());

        mockMvc.perform(get("/devices"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnAllDevices_whenNoDevicesFilterGiven() throws Exception {
        when(deviceService.getDevicesByFilters(null, null, null)).thenReturn(List.of(validDevice, validDevice2));

        mockMvc.perform(get("/devices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void deleteDevice_shouldReturnNoContent() throws Exception {
        doNothing().when(deviceService).deleteDeviceById(1L);

        mockMvc.perform(delete("/devices/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteDevice_shouldReturnNotFound() throws Exception {
        doThrow(new NoSuchElementException()).when(deviceService).deleteDeviceById(1L);

        mockMvc.perform(delete("/devices/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteDevice_shouldReturnBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Device is in use")).when(deviceService).deleteDeviceById(1L);

        mockMvc.perform(delete("/devices/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Device is in use"));
    }
}

