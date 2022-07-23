package org.course.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.course.service.PictureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class PictureController {
    @Value("${url.base}")
    private String baseUrl;
    @Value("${url.api-key}")
    private String apiKey;

    private final PictureService pictureService;

    @GetMapping("/pictures/{sol}/largest")
    public Mono<byte[]> getLargestPicture(@PathVariable Integer sol) {
        return pictureService.getLargestPicture(buildUrl(sol))
                .flatMap(url -> WebClient.create(url)
                        .mutate()
                        .codecs(config -> config.defaultCodecs().maxInMemorySize(10_000_000))
                        .build()
                        .get()
                        .retrieve()
                        .bodyToMono(byte[].class));
    }


    private String buildUrl(Integer sol) {
        return UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("sol", sol)
                .queryParam("api_key", apiKey)
                .build()
                .toString();
    }
}
