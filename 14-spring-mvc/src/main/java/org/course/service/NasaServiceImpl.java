package org.course.service;

import lombok.AllArgsConstructor;
import org.course.entity.Photo;
import org.course.entity.Photos;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Comparator;

@Service
@AllArgsConstructor
public class NasaServiceImpl implements NasaService {
    private final RestTemplate rt;

    @Cacheable("largestPicture")
    public String getLargestPictureUrl(Integer sol) {
        var uri = UriComponentsBuilder.fromUriString("https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos")
                .queryParam("sol", sol)
                .queryParam("api_key", "DEMO_KEY")
                .build().toUri();

        return rt.getForObject(uri, Photos.class)
                .getPhotos()
                .stream()
                .parallel()
                .map(this::setImageSize)
                .max(Comparator.comparingLong(Photo::getSize))
                .map(Photo::getUrl)
                .orElseThrow(IllegalArgumentException::new);
    }

    private Photo setImageSize(Photo photo) {
        var location = rt.headForHeaders(photo.getUrl()).getLocation();
        if (location == null) {
            throw new IllegalArgumentException();
        } else {
            var size = rt.headForHeaders(location).getContentLength();
            photo.setSize(size);
            return photo;
        }
    }
}
