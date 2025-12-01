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
    @JsonProperty("cannes_best_actor_won")
    private int cannesBestActorWon;
    @JsonProperty("volpi_cup_won")
    private int volpiCupWon;
    @JsonProperty("bafta_nominated")
    private int baftaNominated;
    @JsonProperty("bafta_won")
    private int baftaWon;
    @JsonProperty("kennedy_center_honors")
    private int kennedyCenterHonors;
    @JsonProperty("national_board_of_review_won")
    private int nationalBoardOfReviewWon;
    @JsonProperty("nyfcc_won")
    private int nyfccWon;
    @JsonProperty("walk_of_fame_stars")
    private int walkOfFameStars;
    @JsonProperty("berlin_silver_bear_won")
    private int berlinSilverBearWon;
    private String notes;
    @JsonProperty("grammys_won")
    private int grammysWon;
    @JsonProperty("grammy_lifetime_achievement")
    private int grammyLifetimeAchievement;
    @JsonProperty("cecil_b_demille_award")
    private int cecilBDemilleAward;
    @JsonProperty("jean_hersholt_humanitarian_award")
    private int jeanHersholtHumanitarianAward;
    @JsonProperty("national_film_registry_inducted")
    private int nationalFilmRegistryInducted;
    @JsonProperty("david_di_donatello_won")
    private int davidDiDonatelloWon;
    @JsonProperty("san_sebastian_won")
    private int sanSebastianWon;
    @JsonProperty("presidential_medal_of_freedom")
    private int presidentialMedalOfFreedom;

}
