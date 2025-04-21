package com.tripper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Main application class for the Tripper application.
 * This class serves as the entry point for the Spring Boot application.
 * It uses @SpringBootApplication to enable autoconfiguration, component scanning,
 * and configuration properties support.
 *
 * @see SpringBootApplication
 * @see EnableCaching
 * */
@EnableCaching
@SpringBootApplication
public class Application {

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);

	}
}
