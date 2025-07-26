package com.anderson.rinha_backend_2025_v1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients
@SpringBootApplication
public class RinhaBackend2025V1Application {

	public static void main(String[] args) {
		SpringApplication.run(RinhaBackend2025V1Application.class, args);
	}

}
