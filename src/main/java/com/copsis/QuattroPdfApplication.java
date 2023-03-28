package com.copsis;

import java.util.TimeZone;

import jakarta.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QuattroPdfApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuattroPdfApplication.class, args);
	}
	
	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("America/Mexico_City"));
	}
}
