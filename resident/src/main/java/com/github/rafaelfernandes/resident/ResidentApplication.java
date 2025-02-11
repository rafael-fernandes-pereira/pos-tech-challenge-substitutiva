package com.github.rafaelfernandes.resident;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ResidentApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResidentApplication.class, args);
	}

}
