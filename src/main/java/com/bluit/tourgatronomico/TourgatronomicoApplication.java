package com.bluit.tourgatronomico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TourgatronomicoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TourgatronomicoApplication.class, args);
	}

}
