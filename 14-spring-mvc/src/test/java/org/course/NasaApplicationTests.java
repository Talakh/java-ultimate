package org.course;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class NasaApplicationTests {

    @Autowired
    private RestTemplate rt;

    @Test
    void post() {
        var response = rt.postForEntity(
                "http://93.175.204.87:8080/users",
                new User("Joe", "Biden"),
                JsonNode.class);
        System.out.println(response);
    }

    @AllArgsConstructor
    @Data
    public static class User {
        private String firstName;
        private String lastName;
    }
}
