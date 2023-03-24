package com.task.automatic.irrigation.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AutomaticIrrigationSystemApplication {

	public static void main(String[] args) {
		System.out.println("Server is running");
		SpringApplication.run(AutomaticIrrigationSystemApplication.class, args);
	}
}
