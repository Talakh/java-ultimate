package org.course.http;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    @SneakyThrows
    public static void main(String[] args) {
        try (var socket = new Socket("localhost" , 8080)) {
            var outputStream = socket.getOutputStream();
            var writer = new PrintWriter(new OutputStreamWriter(outputStream));
            writer.println("GET /hello?name=Oleksii HTTP/1.1");
            writer.println("Host: 93.175.204.87");
            writer.println();
            writer.flush();

            BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;

            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
