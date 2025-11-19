package com.example.demo.service;

import com.example.demo.model.ActorFilmography;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ActorFilmographyService {

    private final List<ActorFilmography> actorFilmographies;
    private static final String RESOURCE_FILE = "actor-movies-sample.json";

    public ActorFilmographyService() {
        this.actorFilmographies = loadActorFilmographiesFromJson(RESOURCE_FILE);
    }

    public ActorFilmographyService(String resourceFile) {
        if (StringUtils.isBlank(resourceFile)) {
            resourceFile = RESOURCE_FILE;
        }
        this.actorFilmographies = loadActorFilmographiesFromJson(resourceFile);
    }

    private List<ActorFilmography> loadActorFilmographiesFromJson(String resourceFile) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceFile);

            if (inputStream == null) {
                throw new IOException("Could not find "+resourceFile);
            }

            return objectMapper.readValue(inputStream, new TypeReference<ArrayList<ActorFilmography>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Checks if there are any movies with duplicate titles in the collection.
     *
     * @return A map where the key is the duplicate title and the value is the count of occurrences
     */
    public Map<String, Integer> findDuplicateActors() {
        Map<String, Integer> actorsCounts = new HashMap<>();
        Map<String, Integer> duplicates = new HashMap<>();

        // Count occurrences of each title
        for (ActorFilmography actorFilmography : actorFilmographies) {
            String actorName = actorFilmography.getActorProfile().getName();
            actorsCounts.put(actorName, actorsCounts.getOrDefault(actorName, 0) + 1);
        }

        // Filter out titles that appear more than once
        for (Map.Entry<String, Integer> entry : actorsCounts.entrySet()) {
            if (entry.getValue() > 1) {
                duplicates.put(entry.getKey(), entry.getValue());
            }
        }

        return duplicates;
    }

    /**
     * Checks if there are any duplicate actors in the collection.
     *
     * @return true if there are duplicate actors, false otherwise
     */
    public boolean hasDuplicateActors() {
        return !findDuplicateActors().isEmpty();
    }

    public List<ActorFilmography> filterActorFilmographies(String actorName, Integer year,
                                    Integer oscarsNominated, Integer oscarsWon) {

        List<ActorFilmography> filteredActorFilmographies = actorFilmographies.stream()
                .filter(af -> actorName == null || af.isActorName(actorName))
                .filter(af -> year == null      || af.getActorProfile() != null && af.getActorProfile().isInRange(year))
                .filter(af -> oscarsNominated == null || af.isOscarsNominated(oscarsNominated))
                .filter(af -> oscarsWon == null      ||  af.isOscarsWon(oscarsWon))
                .toList();
        return filteredActorFilmographies;
    }

    /*
    public List<ActorFilmography> numberMovies(List<ActorFilmography> actorFilmographies) {
        if (actorFilmographies == null) {
            return new ArrayList<>();
        }

        final java.util.concurrent.atomic.AtomicInteger idx = new java.util.concurrent.atomic.AtomicInteger(1);
        actorFilmographies.stream()
                .filter(Objects::nonNull)
                .forEach(m -> m.setNum(idx.getAndIncrement()));
        return actorFilmographies;
    }
     */
}
