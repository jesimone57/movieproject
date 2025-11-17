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
    private List<String> genres = new ArrayList<>();
    private List<Actor> actors = new ArrayList<>();

    @JsonProperty("oscars_nominated")
    private int oscarsNominated;
    @JsonProperty("oscars_won")
    private int oscarsWon;
    @JsonProperty("oscars_won_details")
    private List<String> oscarsWonDetails = new ArrayList<>();

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

    public  boolean isActor(String filterCriteria) {
        if (actors != null && !actors.isEmpty() && !filterCriteria.isBlank()) {
            List<String> searchTokens = tokenizeFilterCriteria(filterCriteria);
            if (!searchTokens.isEmpty()) {
                boolean result = true;
                for (String token : searchTokens) {
                    result = result && actors.stream().anyMatch(i -> i.getName() != null && StringUtils.containsIgnoreCase(i.getName(), token));
                }
                return result;
            }
        }
        return false;
    }

    public  boolean isOscarsWonDetail(String detail) {
        if (oscarsWonDetails != null && !oscarsWonDetails.isEmpty() && !detail.isBlank()) {
            List<String> searchTokens = tokenizeFilterCriteria(detail);
            if (!searchTokens.isEmpty()) {
                boolean result = true;
                for (String token : searchTokens) {
                    result = result && oscarsWonDetails.stream().anyMatch(i -> StringUtils.containsIgnoreCase(i, token));
                }
                return result;
            }
        }
        return false;
    }

    public boolean isGenre(String genre) {
        if (this.genres != null && !this.genres.isEmpty() && !genre.isBlank()) {
            List<String> searchTokens = tokenizeFilterCriteria(genre);
            if (!searchTokens.isEmpty()) {
                boolean result = true;
                for (String token : searchTokens) {
                    result = result && genres.stream().anyMatch(i -> StringUtils.containsIgnoreCase(i, token));
                }
                return result;
            }
        }
        return false;
    }

    public boolean isInRange(Integer yearStart, Integer yearEnd) {
        return yearStart != null && this.year >= yearStart && yearEnd != null && this.year <= yearEnd;
    }

    private static List<String> tokenizeFilterCriteria(String filterCriteria) {
        List<String> searchTokens = new ArrayList<>();
        // Collect search tokens by splitting on commas
        String[] parts = filterCriteria.split(",");
        for (String part : parts) {
            if (!StringUtils.isBlank(part)) {
                searchTokens.add(part.trim());
            }
        }
        return searchTokens;
    }
}
