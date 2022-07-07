package org.course.controller;

import lombok.AllArgsConstructor;
import org.course.service.NasaService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class NasaController {
    private NasaService nasaService;

    @GetMapping("/pictures/{sol}/largest")
    public ResponseEntity<Object> redirectToLargestPicture(@PathVariable Integer sol) {
        return ResponseEntity
                .status(HttpStatus.PERMANENT_REDIRECT)
                .header(HttpHeaders.LOCATION, nasaService.getLargestPictureUrl(sol))
                .build();
    }
}
