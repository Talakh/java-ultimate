package org.course;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;

import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


public class Main {
    private static final String HTTPS = "https";

    @SneakyThrows
    public static void main(String[] args) {
        getImages().stream()
                .parallel()
                .map(url -> Pair.of(url, getSize(url)))
                .max(Comparator.comparingLong(Pair::getValue))
                .ifPresent(System.out::println);
    }

    @SneakyThrows
    private static int getSize(URL url) {
        var location = getLocation(url);
        var size = getContentLength(new URL(location));
        return Integer.parseInt(size);
    }

    @SneakyThrows
    private static List<URL> getImages() {
        var url = new URL("https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=16&api_key=DEMO_KEY");

        @Cleanup var socket = createSocket(url);
        @Cleanup var writer = new PrintWriter(socket.getOutputStream());
        @Cleanup var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        writer.println("""
                GET %s?%s HTTP/1.1
                Host: %s
                """.formatted(url.getPath(), url.getQuery(), url.getHost()));
        writer.flush();

        var body = parseBody(reader);

        return new ObjectMapper().readTree(body)
                .findValues("img_src").stream()
                .map(JsonNode::textValue)
                .map(Main::createURL)
                .toList();
    }

    @SneakyThrows
    private static String getLocation(URL url) {
        @Cleanup var socket = createSocket(url);
        @Cleanup var writer = new PrintWriter(socket.getOutputStream());

        writer.println("""
                HEAD %s HTTP/1.1
                Host: %s
                """.formatted(url.getPath(), url.getHost()));
        writer.flush();

        return findHeader(socket.getInputStream(), "Location");
    }

    @SneakyThrows
    private static String getContentLength(URL url) {
        @Cleanup var socket = createSocket(url);
        @Cleanup var writer = new PrintWriter(socket.getOutputStream());

        writer.println("""
                HEAD %s HTTP/1.1
                Host: %s
                """.formatted(url.getPath(), url.getHost()));
        writer.flush();

        return findHeader(socket.getInputStream(), "Content-Length");
    }

    private static URL createURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @SneakyThrows
    private static String parseBody(BufferedReader reader) {
        var json = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("{") || line.contains("}")) {
                json.append(line);
            }
            if (!json.isEmpty() && line.isBlank()) {
                break;
            }
        }
        return json.toString();
    }

    @SneakyThrows
    private static Socket createSocket(URL url) {
        if (url.getProtocol().equals(HTTPS)) {
            return SSLSocketFactory.getDefault().createSocket(url.getHost(), url.getDefaultPort());
        } else {
            return new Socket(url.getHost(), url.getDefaultPort());
        }
    }

    @SneakyThrows
    private static String findHeader(InputStream is, String headerName) {
        @Cleanup var reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = reader.readLine()) != null && !line.startsWith(headerName)) ;

        return Optional.ofNullable(line)
                .map(s -> s.substring(s.indexOf(" ")))
                .orElseThrow(IllegalAccessError::new);
    }
}
