package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Top-level entry for actor-movies-sample.json consisting of an actor profile and their filmography.
 */
@Data
public class ActorFilmography {

    @JsonProperty("actor_profile")
    private ActorProfile actorProfile;

    private List<ActorMovie> filmography;

    public boolean isActorName(String actorName) {
        if (this.actorProfile != null && actorProfile.getName() != null && !actorName.isBlank()) {
            return StringUtils.containsIgnoreCase(this.actorProfile.getName(), actorName.trim());
        }
        return false;
    }

    public boolean isOscarsNominated(Integer oscarsNominated) {
        if (actorProfile != null && actorProfile.getActorAwards() != null) {
            return actorProfile.getActorAwards().getOscarsNominated() == oscarsNominated;
        }
        return false;
    }

    public boolean isOscarsWon(Integer oscarsWon) {
        if (actorProfile != null && actorProfile.getActorAwards() != null) {
            return actorProfile.getActorAwards().getOscarsWon() == oscarsWon;
        }
        return false;
    }
}
