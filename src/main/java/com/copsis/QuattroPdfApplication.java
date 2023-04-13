package com.copsis;

import java.util.TimeZone;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QuattroPdfApplication {

	@Value("${TZ:America/Mexico_City}")
    private String timeZone;
	
	public static void main(String[] args) {
		SpringApplication.run(QuattroPdfApplication.class, args);
	}
	
	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
	}
}
