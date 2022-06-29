package org.course;

import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Collectors;

public class Main {

    @SneakyThrows
    public static void main(String[] args) {
//        var request = HttpRequest
//                .newBuilder(URI.create("http://93.175.204.87:8080/morning"))
//                .header("X-Mood", "")
//                .GET().build();
//        var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println("Response: " + response.body());
        get();
    }


    @SneakyThrows
    private static void post() {
        try (var socket = new Socket("93.175.204.87", 8080);
             var writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            var data = """
                    {
                    "firstName": "Oleksii",
                    "lastName": "Talakh"
                    }
                    """;

            writer.println("POST /here HTTP/1.1");
            writer.println("Host: 93.175.204.87");
            writer.println("Content-Length: " + data.length());
            writer.println("Content-Type: application/json");
            writer.println();
            writer.println(data);
            writer.flush();

            BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            var response = rd.lines().collect(Collectors.joining("\n"));
            System.out.println(response);

        }
    }

    @SneakyThrows
    private static void get() {
        try (var socket = new Socket("93.175.204.87", 8080)) {
            var writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            writer.println("GET /morning HTTP/1.1");
            writer.println("Host: 93.175.204.87");
            writer.println("X-Mood: hungry");
            writer.println("Cookie: CUSTOM_ID=Oleksii T.");
            writer.println();
            writer.flush();

            BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            var response = rd.lines().collect(Collectors.joining("\n"));
            System.out.println(response);

        }
    }
}
