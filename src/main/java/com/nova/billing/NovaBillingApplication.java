package com.nova.billing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;

@SpringBootApplication
@EnableBatchProcessing
public class NovaBillingApplication {

	public static void main(String[] args) {
		SpringApplication.run(NovaBillingApplication.class, args);
	}

}
