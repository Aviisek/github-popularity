package com.redcare.challenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ClockConfig {

    @Bean
    Clock systemUtcClock(){
        return Clock.systemUTC();
    }
}
