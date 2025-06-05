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

    private ObjectMapper objectMapper = new ObjectMapper();

    private DeviceDTO validDevice;
    private DeviceDTO invalidDevice;

    @BeforeEach
    void setUp() {
        validDevice = new DeviceDTO(1L, "Device1", "BrandA", DeviceState.AVAILABLE);
        invalidDevice = new DeviceDTO(null, "", "", null); // invalid name, brand, and state
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
                .andExpect(status().isBadRequest());
    }

    @Test
    void getDevice_found() throws Exception {
        when(deviceService.getDeviceById(1L)).thenReturn(validDevice);

        mockMvc.perform(get("/devices/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Device1"));
    }

    @Test
    void getDevice_notFound() throws Exception {
        when(deviceService.getDeviceById(1L)).thenReturn(null);

        mockMvc.perform(get("/devices/1"))
                .andExpect(status().isNotFound());
    }

}

