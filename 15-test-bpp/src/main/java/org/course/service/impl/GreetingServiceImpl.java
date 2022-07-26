package org.course.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.course.annotation.Trimmed;
import org.course.service.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Trimmed
@Service
public class GreetingServiceImpl implements GreetingService {

    @Autowired
    private RestTemplate rt;

    @Override
    public String createGreeting(String name) {
        log.info("Input name {}", name);
        return "    Hello, %s!".formatted(name);
    }
}
