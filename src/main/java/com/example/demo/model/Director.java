package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Director {
    private String name;
    @JsonProperty("imdb_person_url")
    private String imdbPersonUrl;
}
