package org.course.service;

import reactor.core.publisher.Mono;

public interface PictureService {

    Mono<String> getLargestPicture(String url);
}
