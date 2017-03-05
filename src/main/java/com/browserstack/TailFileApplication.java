package com.browserstack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(
		basePackages = { "com.browserstack" })
public class TailFileApplication {

	public static void main(String[] args) {
		SpringApplication.run(TailFileApplication.class, args);
	}
}
