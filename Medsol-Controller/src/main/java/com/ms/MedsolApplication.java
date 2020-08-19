package com.ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan( basePackages = {"com"} )
public class MedsolApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(MedsolApplication.class, args);
	}

}
