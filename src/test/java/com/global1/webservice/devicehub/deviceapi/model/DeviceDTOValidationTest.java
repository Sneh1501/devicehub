package com.global1.webservice.devicehub.deviceapi.model;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeviceDTOValidationTest {
    private static Validator validator;

    private final DeviceDTO devicedto = new DeviceDTO(1L, "Device1", "BrandA", DeviceState.AVAILABLE);

    @BeforeAll
    public static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validDeviceDTO_ShouldPassValidation() {
        Set<ConstraintViolation<DeviceDTO>> violations = validator.validate(devicedto);
        assertTrue(violations.isEmpty(), "DTO should be valid");
    }

    @Test
    void blankNameShouldFailValidation() {
        devicedto.setName(" ");
        Set<ConstraintViolation<DeviceDTO>> violations = validator.validate(devicedto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void nullBrandShouldFailValidation() {
        devicedto.setBrand(null); // null

        Set<ConstraintViolation<DeviceDTO>> violations = validator.validate(devicedto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("brand")));
    }

    @Test
    void nullStateShouldFailValidation() {
        devicedto.setState(null); // null

        Set<ConstraintViolation<DeviceDTO>> violations = validator.validate(devicedto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("state")));
    }
}
