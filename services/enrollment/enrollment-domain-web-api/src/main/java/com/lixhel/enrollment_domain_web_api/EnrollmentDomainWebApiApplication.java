package com.lixhel.enrollment_domain_web_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class EnrollmentDomainWebApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnrollmentDomainWebApiApplication.class, args);
	}

}
