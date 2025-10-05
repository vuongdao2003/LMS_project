package com.example.onlinepay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.example.onlinepay.entity", "com.example.identity.entity"})
public class OnlinePayApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlinePayApplication.class, args);
	}

}
