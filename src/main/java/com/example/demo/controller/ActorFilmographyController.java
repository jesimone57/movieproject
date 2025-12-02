package com.example.demo.controller;

import com.example.demo.model.ActorFilmography;
import com.example.demo.service.ActorFilmographyService;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/filmographies")
public class ActorFilmographyController {

    private final ActorFilmographyService actorFilmographyService;

    public ActorFilmographyController(ActorFilmographyService actorFilmographyService) {
        this.actorFilmographyService = actorFilmographyService;
    }

    @GetMapping("/search")
    public List<ActorFilmography> searchByActorName(
            @RequestParam(value = "name",      required = false) String actorName,
            @RequestParam(value = "year",      required = false) Integer year,
            @RequestParam(value = "oscarsNominated", required = false) Integer oscarsNominated,
            @RequestParam(value = "oscarsWon", required = false) Integer oscarsWon,
            @RequestParam(value = "filmListSearchText", required = false) String filmographySearchText,
            @RequestParam(value = "sort", required = false) String sort) {
        return actorFilmographyService.filterActorFilmographies(actorName, year, oscarsNominated, oscarsWon, filmographySearchText, sort);
    }

    // GET /api/filmographies/duplicates -> map of duplicate title to count
    @GetMapping("/duplicates")
    public Map<String, Integer> getDuplicateTitles() {
        return actorFilmographyService.findDuplicateActors();
    }

    // GET /api/movies/duplicates/exists -> true/false
    @GetMapping("/duplicates/exists")
    public boolean hasDuplicateTitles() {
        return actorFilmographyService.hasDuplicateActors();
    }
}
