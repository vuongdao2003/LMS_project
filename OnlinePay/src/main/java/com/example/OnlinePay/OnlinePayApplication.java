package com.example.OnlinePay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.example.OnlinePay.entity", "com.example.demo.entity"})
public class OnlinePayApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlinePayApplication.class, args);
	}

}
