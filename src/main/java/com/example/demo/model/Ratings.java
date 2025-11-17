package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Ratings {
    private double imdb;
    @JsonProperty("rotten_tomatoes_critic")
    private String rottenRomatoesCriticScore;
    @JsonProperty("rotten_tomatoes_popcorn")
    private String rottenTomatoesPopcornScore;
}
