package org.course.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.course.entity.Picture;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PictureServiceImpl implements PictureService {

    @Override
    public Mono<String> getLargestPicture(String url) {
        return WebClient.create(url)
                .get()
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(JsonNode.class))
                .flatMapIterable(node -> node.findValues("img_src"))
                .map(JsonNode::asText)
                .flatMap(this::buildImage)
                .reduce((p1, p2) -> p1.size() > p2.size() ? p1 : p2)
                .map(Picture::url);
    }

    private Mono<Picture> buildImage(String url) {
        return getLocation(url)
                .flatMap(this::getContentLength)
                .map(length -> new Picture(url, length));
    }

    private Mono<String> getLocation(String url) {
        return WebClient.create(url)
                .head()
                .exchangeToMono(ClientResponse::toBodilessEntity)
                .map(r -> r.getHeaders().getLocation().toString());
    }

    private Mono<Long> getContentLength(String url) {
        return WebClient.create(url)
                .head()
                .exchangeToMono(ClientResponse::toBodilessEntity)
                .map(r -> r.getHeaders().getContentLength());
    }
}
