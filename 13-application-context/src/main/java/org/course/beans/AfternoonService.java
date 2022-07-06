package org.course.beans;

import org.course.annotations.Bean;

@Bean
public class AfternoonService implements GreetingService {

    @Override
    public void greeting() {
        System.out.println("Good afternoon!");
    }
}
