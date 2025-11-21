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
    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("year_of_first_film")
    private Integer yearOfFirstFilm;
    @JsonProperty("year_of_last_film")
    private Integer yearOfLastFilm;
    @JsonProperty("number_of_feature_films_made")
    private Integer numberOfFeatureFilms;

    @JsonProperty("actor_awards")
    private ActorAwards actorAwards;

    public boolean isInRange(Integer year) {
        return this.yearBorn != null && yearDied != null && year != null && year <= yearDied && year >= this.yearBorn;
    }

    public Integer getCareerLengthInYears() {
        if (yearBorn != null && yearDied != null && yearBorn < yearDied) {
            return yearDied - yearBorn;
        }
        return null;
    }

    public Integer getAgeLengthInYears() {
        if (yearOfFirstFilm != null && yearOfLastFilm != null && yearOfFirstFilm <= yearOfLastFilm) {
            return yearOfLastFilm - yearOfFirstFilm;
        }
        return null;
    }
}
