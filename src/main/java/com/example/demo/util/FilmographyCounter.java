package com.example.demo.util;

import com.example.demo.model.ActorFilmography;
import com.example.demo.service.ActorFilmographyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilmographyCounter {
    private static final Logger logger = LoggerFactory.getLogger(FilmographyCounter.class);

    public static void main(String[] args) {
        ActorFilmographyService actorFilmographyService = new ActorFilmographyService();
        for (ActorFilmography af: actorFilmographyService.filterActorFilmographies(null, null, null, null)) {
            logger.info("actor-filmographies for {} : {}", af.getActorProfile().getName(), af.getFilmography().size());
        }

    }
}
