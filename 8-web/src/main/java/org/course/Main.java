package org.course;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Main {
    private static final String URL = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=15&api_key=DEMO_KEY";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        largestPictureByHttpClient();
        long end = System.currentTimeMillis();
        System.out.println();
        System.out.println(end - start);
    }

    public static void largestPictureByRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForEntity(URL, JsonNode.class)
                .getBody()
                .findValues("img_src").stream()
                .map(JsonNode::textValue)
                .map(uri -> Map.entry(uri, getImageSize(uri)))
                .min(Map.Entry.comparingByValue())
                .ifPresent(e -> System.out.format("Url: %s, size: %d", e.getKey(), e.getValue()));
    }

    public static long getImageSize(String url) {
        return new RestTemplate().headForHeaders(url).getContentLength();
    }

    @SneakyThrows
    public static void largestPictureByHttpClient() {
        HttpClient httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        HttpRequest getUrlsRequest = HttpRequest.newBuilder(URI.create(URL))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(getUrlsRequest, HttpResponse.BodyHandlers.ofString());
        JsonNode jsonNode = mapper.readTree(response.body());
        jsonNode.findValues("img_src").stream()
                .parallel()
                .map(JsonNode::textValue)
                .map(Main::createHeadRequest)
                .map(request -> Map.entry(request.uri().toString(), getImageSize(httpClient, request)))
                .max(Map.Entry.comparingByValue())
                .ifPresent(e -> System.out.format("Url: %s, size: %d", e.getKey(), e.getValue()));
    }

    @SneakyThrows
    private static long getImageSize(HttpClient httpClient, HttpRequest request) {
        return httpClient.send(request, HttpResponse.BodyHandlers.discarding())
                .headers()
                .firstValueAsLong(HttpHeaders.CONTENT_LENGTH)
                .orElse(0);
    }

    @SneakyThrows
    private static HttpRequest createHeadRequest(String url) {
        return HttpRequest.newBuilder(URI.create(url))
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();
    }

}
