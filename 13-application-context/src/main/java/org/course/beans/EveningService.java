package org.course.beans;

import org.course.annotations.Bean;

@Bean("evening")
public class EveningService implements GreetingService{

    @Override
    public void greeting() {
        System.out.println("Good evening!");
    }
}
