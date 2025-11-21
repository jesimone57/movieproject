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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

@Service
public class ActorFilmographyService {

    private Logger logger = LoggerFactory.getLogger(ActorFilmographyService.class);
    private final List<ActorFilmography> actorFilmographies;
    private static final String RESOURCE_FILE = "actor-movies-sample.json";
    private static final String FILMOGRAPHIES_FOLDER = "actor-filmographies";

    public ActorFilmographyService() {
        this.actorFilmographies = new ArrayList<>();
        loadActorFilmographiesFromFolder();
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
                String message = "Resource not found: " + resourceFile;
                logger.error(message);
                throw new IOException(message);
            }

            return objectMapper.readValue(inputStream, new TypeReference<ArrayList<ActorFilmography>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Loads any JSON resources found under the given folder on the classpath and appends them
     * to the current list of actorFilmographies. This method is resilient to files containing
     * either a single ActorFilmography object or an array of ActorFilmography objects.
     * Malformed files are skipped.
     *
     * @param folder the classpath folder containing actor filmography JSON files
     */
    public void loadActorFilmographiesFromFolder(String folder) {
        if (folder == null || StringUtils.isBlank(folder)) {
            throw new IllegalArgumentException("folder cannot be null or empty");
        }

        logger.info("Loading ActorFilmographies from resource folder: " + folder);
        int addedFileCount = 0;
        ObjectMapper objectMapper = new ObjectMapper();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources("classpath*:" + folder + "/**/*.json");
            for (Resource resource : resources) {
                if (resource == null || !resource.exists()) {
                    continue;
                }
                String resourceFileName = resource.getFile().getName();

                // IMPORTANT: Use the Resource to open the stream with the correct path
                try (InputStream is = resource.getInputStream()) {
                    // Try as a single object first
                    try {
                        ActorFilmography single = objectMapper.readValue(is, ActorFilmography.class);
                        if (single != null) {
                            this.actorFilmographies.add(single);
                            addedFileCount++;
                            logger.info("\t" + addedFileCount + " added as single entry: " + resourceFileName);
                        }
                    } catch (IOException singleEx) {
                        // If single parse failed, try as an array/list
                        try (InputStream is2 = resource.getInputStream()) {
                            List<ActorFilmography> list = objectMapper.readValue(is2, new TypeReference<ArrayList<ActorFilmography>>() {});
                            if (list != null && !list.isEmpty()) {
                                this.actorFilmographies.addAll(list);
                                addedFileCount++;
                                logger.info("\t" + addedFileCount + " added as multiple entries: " + resourceFileName);
                            }
                        } catch (IOException listEx) {
                            logger.info("\t" + "Skip malformed resource " + resourceFileName + " and continue (malformed or unexpected structure)");

                        }
                    }
                }
            }
            logger.info("Loaded "+ addedFileCount + " ActorFilmographies from resource folder: " + folder);
        } catch (IOException e) {
            // If we cannot scan the folder, simply ignore and continue with existing data
            // Optionally log in the future
        }
    }

    /**
     * Convenience method to load from the default actor-filmographies folder on the classpath.
     */
    public void loadActorFilmographiesFromFolder() {
        loadActorFilmographiesFromFolder(FILMOGRAPHIES_FOLDER);
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
