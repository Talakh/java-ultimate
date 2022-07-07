package org.course.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Photo {
    @JsonProperty("img_src")
    private String url;
    private Long size;
}
