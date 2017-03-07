package com.browserstack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * This is starting point of the application.
 * @author raghunandan.gupta
 *
 */
@SpringBootApplication
@ComponentScan(
		basePackages = { "com.browserstack" })
public class CommandApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommandApplication.class, args);
	}
}
