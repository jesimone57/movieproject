package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Represents an actor profile entry from actor-movies-sample.json
 * Named per request: each actor is modeled as MovieActor.
 */
@Data
public class ActorProfile {
    private String name;

    @JsonProperty("year_born")
    private Integer yearBorn;

    @JsonProperty("year_died")
    private Integer yearDied;

    @JsonProperty("imdb_url")
    private String imdbUrl;

    @JsonProperty("actor_awards")
    private ActorAwards actorAwards;

    public boolean isInRange(Integer year) {
        return this.yearBorn != null && yearDied != null && year != null && year <= yearDied && year >= this.yearBorn;
    }
}
