package com.global1.webservice.devicehub.deviceapi.model;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class DeviceDTO {
    private Long id;

    @NotNull(message= "Device name is required")
    private String name;

    @NotNull(message="Brand name is required")
    private String brand;

    @NotNull(message="Device state is required")
    private DeviceState state;

    private LocalDateTime creationTime;

    public DeviceDTO() {
    }

    public DeviceDTO(Long id, String name, String brand, DeviceState state, LocalDateTime creationTime) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.state = state;
        this.creationTime = creationTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public DeviceState getState() {
        return state;
    }

    public void setState(DeviceState state) {
        this.state = state;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }
}
