package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Represents a single film entry within an actor's filmography from actor-movies-sample.json
 * Named per request: each film is modeled as ActorMovie.
 */
@Data
public class ActorMovie {
    private String title;
    private Integer year;
    private String plot;
    private String role;
    @JsonProperty("role_description")
    private String roleDescription;
    private String awards; // can be null

    private Ratings ratings;

    @JsonProperty("imdb_url")
    private String imdbUrl;

    public boolean isPlotText(String plotText) {
        if (plot != null && !plot.isBlank() && plotText != null && !plotText.isBlank()) {
            return StringUtils.containsIgnoreCase(plot, plotText.trim());
        }
        return false;
    }

    public boolean isRoleDescriptionText(String roleDescriptionText) {
        if (roleDescription != null && !roleDescription.isBlank() && roleDescriptionText != null && !roleDescriptionText.isBlank()) {
            return StringUtils.containsIgnoreCase(roleDescription, roleDescriptionText.trim());
        }
        return false;
    }

    public boolean isRoleText(String roleText) {
        if (role != null && !role.isBlank() && roleText != null && !roleText.isBlank()) {
            return StringUtils.containsIgnoreCase(role, roleText.trim());
        }
        return false;
    }

    public boolean isTitleText(String titleText) {
        if (title != null && !title.isBlank() && titleText != null && !titleText.isBlank()) {
            return StringUtils.containsIgnoreCase(title, titleText.trim());
        }
        return false;
    }
    

}
