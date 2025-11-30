package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Lifetime stats for an actor (wins, nominations) from actor-movies-sample.json
 */
@Data
public class ActorAwards {
    @JsonProperty("oscars_honorary")
    private int oscarsHonorary;
    @JsonProperty("oscars_nominated")
    private int oscarsNominated;
    @JsonProperty("oscars_won")
    private int oscarsWon;
    @JsonProperty("golden_globes_nominated")
    private int goldenGlobessNominated;
    @JsonProperty("golden_globes_won")
    private int goldenGlobessWon;
    @JsonProperty("emmys_won")
    private int emmysWon;
    @JsonProperty("cannes_won")
    private int cannesWon;
    @JsonProperty("volpi_cup_won")
    private int volpiCupWon;
    @JsonProperty("bafta_nominated")
    private int baftaNominated;
    @JsonProperty("kennedy_center_honors")
    private int kennedyCenterHonors;
    @JsonProperty("national_board_of_review_won")
    private int nationalBoardOfReviewWon;
}
