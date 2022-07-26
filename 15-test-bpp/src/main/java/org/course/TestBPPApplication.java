package org.course;

import lombok.extern.slf4j.Slf4j;
import org.course.annotation.EnableStringTrimming;
import org.course.service.GreetingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@Slf4j
@EnableStringTrimming
@SpringBootApplication
public class TestBPPApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(TestBPPApplication.class, args);
        var greetingService = context.getBean(GreetingService.class);
        var greeting = greetingService.createGreeting("   Oleksii   ");
        log.info("Greeting: {}", greeting);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
