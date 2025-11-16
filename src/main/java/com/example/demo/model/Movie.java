package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class Movie {

    private  int num;
    private String title;
    private int year;
    private Director director;
    @JsonProperty("production_studio")
    private String studio;
    @JsonProperty("mpaa_rating")
    private String mpaaRating;
    @JsonProperty("runtime_minutes")
    private int runtimeMinutes;

    @JsonProperty("budget_usd_millions")
    private Double budget;
    @JsonProperty("gross_revenue_usd_millions")
    private Double revenue;

    private Ratings ratings;
    private List<String> genres;
    private List<Actor> actors;

    @JsonProperty("oscars_nominated")
    private int oscarsNominated;
    @JsonProperty("oscars_won")
    private int oscarsWon;
    @JsonProperty("oscars_won_details")
    private List<String> oscarsWonDetails;

    private String description;
    @JsonProperty("afi_ranking")
    private Integer afiRanking;
    @JsonProperty("imdb_url")
    private String imdbUrl;
    @JsonProperty("rotten_tomatoes_url")
    private String rottenTomatoesUrl;

    public double getImdbRating() {
        return ratings.getImdb();
    }

    public boolean isGenre(String genre) {
        // Handle null or empty data safely
        if (this.genres == null || this.genres.isEmpty() || genre == null) {
            return false;
        }

        // Flatten the movie's genre list into a single searchable string
        String genreText = String.join(" ", this.genres);

        // Collect search tokens by splitting on commas
        String[] parts = genre.split(",");
        List<String> tokens = new ArrayList<>();
        for (String part : parts) {
            if (!StringUtils.isBlank(part)) {
                tokens.add(part.trim());
            }
        }

        // If no tokens derived, fall back to the original containsIgnoreCase behavior
        if (tokens.isEmpty()) {
            return StringUtils.containsIgnoreCase(genreText, genre);
        }

        // Require that ALL tokens are found (case-insensitive) within the movie's genre text
        for (String token : tokens) {
            if (!StringUtils.containsIgnoreCase(genreText, token)) {
                return false;
            }
        }
        return true;
    }

    public boolean isInRange(Integer yearStart, Integer yearEnd) {
        return yearStart != null && this.year >= yearStart && yearEnd != null && this.year <= yearEnd;
    }
}
