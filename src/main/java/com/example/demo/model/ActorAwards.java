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
    @JsonProperty("emmys_nominated")
    private int emmysNominated;
    @JsonProperty("emmys_won")
    private int emmysWon;
    @JsonProperty("cannes_won")
    private int cannesWon;
    @JsonProperty("cannes_best_actor_won")
    private int cannesBestActorWon;
    @JsonProperty("cannes_best_actress_won")
    private int cannesBestActressWon;
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
    @JsonProperty("national_medal_of_arts_won")
    private int nationalMedalOfArtsWon;
    @JsonProperty("david_di_donatello_won")
    private int davidDiDonatelloWon;
    @JsonProperty("san_sebastian_won")
    private int sanSebastianWon;
    @JsonProperty("presidential_medal_of_freedom")
    private int presidentialMedalOfFreedom;
    @JsonProperty("honorary_golden_bear")
    private int honoraryGoldenBear;
    @JsonProperty("saturn_awards_won")
    private int saturnAwardsWon;
    @JsonProperty("disney_legends_inducted")
    private int disneyLegendsInducted;
    @JsonProperty("photoplay_awards_won")
    private int photoplayAwardsWon;
    @JsonProperty("golden_boots_won")
    private int goldenBootsWon;
    @JsonProperty("inventors_hall_of_fame")
    private int inventorsHallOfFame;
    @JsonProperty("independent_spirit_awards_nominated")
    private int independentSpiritAwardsNominated;
    @JsonProperty("sag_lifetime_achievement")
    private int sagLifetimeAchievement;
    @JsonProperty("tony_awards_won")
    private int tonyAwardsWon;
    @JsonProperty("golden_apple_awards_won")
    private int goldenAppleAwardsWon;
    @JsonProperty("screen_actors_guild_presidency")
    private String screenActorsGuildPresidency;
    @JsonProperty("moscow_film_festival_won")
    private int moscowFilmFestivalWon;
    @JsonProperty("afi_life_achievement")
    private int afiLifeAchievement;
    @JsonProperty("box_office_rank")
    private String boxOfficeRank;
    @JsonProperty("venice_film_festival_won")
    private int veniceFilmFestivalWon;
    @JsonProperty("venice_film_festival_career_golden_lion")
    private int veniceFilmFestivalCareerGoldenLion;
    @JsonProperty("civilian_awards")
    private String civilianAwards;
    @JsonProperty("laurel_awards_won")
    private int laurelAwardsWon;
    @JsonProperty("french_legion_of_honour")
    private int frenchLegionOfHonour;
}
