package com.redcare.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.resilience.annotation.EnableResilientMethods;

@SpringBootApplication
@EnableResilientMethods
public class RedcareCodingChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedcareCodingChallengeApplication.class, args);
	}

}
