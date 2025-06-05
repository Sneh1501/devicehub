package com.global1.webservice.devicehub;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Device API", version = "1.0"))
public class DevicehubApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevicehubApplication.class, args);
	}

}
