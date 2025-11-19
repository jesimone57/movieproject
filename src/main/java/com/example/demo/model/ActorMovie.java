package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Represents a single film entry within an actor's filmography from actor-movies-sample.json
 * Named per request: each film is modeled as ActorMovie.
 */
@Data
public class ActorMovie {
    private String title;
    private Integer year;
    private String plot;
    private String awards; // can be null

    private Ratings ratings;

    @JsonProperty("imdb_url")
    private String imdbUrl;
}
