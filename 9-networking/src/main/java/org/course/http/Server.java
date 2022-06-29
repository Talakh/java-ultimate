package org.course.http;

import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Server {

    @SneakyThrows
    public static void main(String[] args) {
        try (var server = new ServerSocket(8080)) {
            while (true) {
                var socket = server.accept();
//            socket.setSoTimeout(2000);
                var msg = readInputStream(socket.getInputStream());
                var request = msg.get(0);
                if (request != null && request.startsWith("GET")) {
                    var url = request.split(" ")[1];
                    var query = url.split("\\?")[1];
                    var paramValue = query.split("=")[1];
                    sendResponse(socket, paramValue);
                }
                System.out.println(msg);
                socket.close();
            }
        }
    }

    @SneakyThrows
    private static void sendResponse(Socket socket, String paramValue) {
        @Cleanup var os = socket.getOutputStream();
        @Cleanup var printWriter = new PrintWriter(new OutputStreamWriter(os));

        printWriter.println("HTTP/1.1 200 OK");
        printWriter.println("Content-Type: text/html");
        printWriter.println("Date: " + ZonedDateTime.now());
        printWriter.println();
        printWriter.println("awdawdaw");
        printWriter.flush();
    }

    @SneakyThrows
    private static List<String> readInputStream(InputStream is) {
        @Cleanup var reader = new BufferedReader(new InputStreamReader(is));
        return reader.lines().toList();
    }
}
