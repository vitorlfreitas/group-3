package com.tripper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Tripper application.
 * This class serves as the entry point for the Spring Boot application.
 * It uses @SpringBootApplication to enable autoconfiguration, component scanning,
 * and configuration properties support.
 *
 * @see SpringBootApplication
 * */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);

	}
}
