package org.course.backson;

import lombok.Data;
import lombok.SneakyThrows;

public class Main {

    @SneakyThrows
    public static void main(String[] args) {

        var person = new BacksonMapper().readObject(
                """
                {
                    "first_name": "Andrii",
                    "last_name": "Shtramak",
                    "city": "Zarichchya"
                }
                """, Person.class);
        System.out.println(person);
    }

    @Data
    public static class Person {
        @PropertyName("first_name")
        private String firstName;
        @PropertyName("last_name")
        private String lastName;
        private String city;
    }

}
