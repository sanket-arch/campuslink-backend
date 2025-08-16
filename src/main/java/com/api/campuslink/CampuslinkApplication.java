package com.api.campuslink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CampuslinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(CampuslinkApplication.class, args);
	}

}
