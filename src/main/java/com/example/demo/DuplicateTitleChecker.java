package com.example.demo;

import com.example.demo.service.MovieService;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple utility class to check for duplicate movie titles in all the movies-yyyy.json files.
 */
public class DuplicateTitleChecker {
    private static Logger logger = LoggerFactory.getLogger(DuplicateTitleChecker.class);

    public static void main(String[] args) {
        MovieService movieService;
        String folder = "movies-by-year/";

        for (int i = 1930; i <= 2007; i++) {
            String filename = folder + "movies-" + i + ".json";
            logger.info("Duplicates checking for resource file " + filename);

            movieService = new MovieService(filename);
            checkForDuplicateTitles(movieService, filename);
        }
        for (int i = 1900; i <= 1920;  i+=10) {
            String filename = folder + "movies-" + i + "s.json";
            logger.info("Duplicates checking for resource file " + filename);

            movieService = new MovieService(filename);
            checkForDuplicateTitles(movieService, filename);
        }
    }

    private static void checkForDuplicateTitles(MovieService movieService, String inputFile) {
        // Check for duplicate titles
        Map<String, Integer> duplicates = movieService.findDuplicateTitles();
        boolean hasDuplicates = movieService.hasDuplicateTitles();

        if (hasDuplicates) {
            System.out.println("\tDuplicate titles found:");
            int duplicatesCount = 0;
            for (Map.Entry<String, Integer> entry : duplicates.entrySet()) {
                System.out.println("\tTitle: '" + entry.getKey() + "' appears " + entry.getValue() + " times");
                duplicatesCount += entry.getValue();
            }
            System.out.println("\tTotal number of duplicate movies: " + duplicatesCount);
            System.out.println("\tTotal number of movies: " + movieService.getAllMovies().size());
        } else {
            System.out.println("\tNo duplicate titles found in " + inputFile);
            System.out.println("\tTotal number of movies: " + movieService.getAllMovies().size());
        }
    }
}
