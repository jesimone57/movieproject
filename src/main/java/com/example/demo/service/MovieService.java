package com.example.demo.service;

import com.example.demo.model.Movie;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    private Logger logger = LoggerFactory.getLogger(MovieService.class);
    private final List<Movie> movies;
    private static final String RESOURCE_FILE = "movies-by-year/movies-1958.json";
    private static final String MOVIES_FOLDER = "movies-by-year";

    public MovieService() {
        this.movies = new ArrayList<>();
        loadMoviesFromFolder();
    }

    public MovieService(String resourceFile) {
        if (StringUtils.isBlank(resourceFile)) {
            resourceFile = RESOURCE_FILE;
        }
        this.movies = loadMoviesFromJson(resourceFile);
    }

    private List<Movie> loadMoviesFromJson(String resourceFile) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceFile);

            if (inputStream == null) {
                throw new IOException("Could not find "+resourceFile);
            }

            return objectMapper.readValue(inputStream, new TypeReference<ArrayList<Movie>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Convenience method to load from the default movies folder on the classpath.
     */
    public void loadMoviesFromFolder() {
        loadMoviesFromFolder(MOVIES_FOLDER);
    }


    /**
     * Loads any JSON resources found under the given folder on the classpath and appends them
     * to the current list of movies. This method is resilient to files containing
     * either a single Movie object or an array of Movie objects.
     * Malformed files are skipped.
     *
     * @param folder the classpath folder containing movie JSON files
     */
    public void loadMoviesFromFolder(String folder) {
        if (folder == null || StringUtils.isBlank(folder)) {
            throw new IllegalArgumentException("folder cannot be null or empty");
        }

        logger.info("Loading Movies from resource folder: " + folder);
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
                        Movie single = objectMapper.readValue(is, Movie.class);
                        if (single != null) {
                            this.movies.add(single);
                            addedFileCount++;
                            logger.info("\t" + addedFileCount + " added as single entry: " + resourceFileName);
                        }
                    } catch (IOException singleEx) {
                        // If single parse failed, try as an array/list
                        try (InputStream is2 = resource.getInputStream()) {
                            List<Movie> list = objectMapper.readValue(is2, new TypeReference<ArrayList<Movie>>() {});
                            if (list != null && !list.isEmpty()) {
                                list = removeDuplicateMovies(list);
                                this.movies.addAll(list);
                                addedFileCount++;
                                logger.info("\t" + addedFileCount + " added as multiple entries: " + resourceFileName);
                            }
                        } catch (IOException listEx) {
                            logger.info("\t" + "Skip malformed resource " + resourceFileName + " and continue (malformed or unexpected structure)");

                        }
                    }
                }
            }
            logger.info("Loaded "+ addedFileCount + " Movies from resource folder: " + folder);
        } catch (IOException e) {
            // If we cannot scan the folder, simply ignore and continue with existing data
            // Optionally log in the future
        }
    }


    public List<Movie> getAllMovies() {
        return filterMovies(null, null, null, null, null,
                null, null, null, null, null);
    }

    public List<String> getAllGenres() {
        // Use streams and lambdas to flatten all movie genres, remove blanks, de-duplicate, and sort
        return this.movies.stream()
                .filter(Objects::nonNull)
                .flatMap(m -> {
                    List<String> genres = m.getGenres();
                    return genres == null ? Stream.<String>empty() : genres.stream();
                })
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .sorted()
                .toList();
    }

    public List<Movie> getMovieByPartialTitle(String title, String sort) {
        if (sort == null) {
            sort = "title";
        }
        return filterMovies(title, null, null, null, null, sort,
                null, null, null, null);
    }

    public List<Movie> removeDuplicateMovies(List<Movie> movies) {
        Set<Movie> uniqueMovies = new HashSet<>();
        uniqueMovies.addAll(movies);
        return new ArrayList<>(uniqueMovies);
    }

    /**
     * Checks if there are any movies with duplicate titles in the collection.
     *
     * @return A map where the key is the duplicate title and the value is the count of occurrences
     */
    public Map<String, Integer> findDuplicateTitles() {
        Map<String, Integer> titleCounts = new HashMap<>();
        Map<String, Integer> duplicates = new HashMap<>();

        // Count occurrences of each title
        for (Movie movie : movies) {
            String title = movie.getTitle();
            titleCounts.put(title, titleCounts.getOrDefault(title, 0) + 1);
        }

        // Filter out titles that appear more than once
        for (Map.Entry<String, Integer> entry : titleCounts.entrySet()) {
            if (entry.getValue() > 1) {
                duplicates.put(entry.getKey(), entry.getValue());
            }
        }

        return duplicates;
    }

    /**
     * Checks if there are any duplicate movie titles in the collection.
     *
     * @return true if there are duplicate titles, false otherwise
     */
    public boolean hasDuplicateTitles() {
        return !findDuplicateTitles().isEmpty();
    }

    public List<Movie> getMovieByGenre(String genre) {
        if (StringUtils.isBlank(genre)) {
            return new ArrayList<>();
        } else {
            return filterMovies(null, genre, null, null, null,
                    "title", null, null, null, null);
        }
    }

    public List<Movie> getMovieByRating(Double imdbRating) {
        return filterMovies(null, null, imdbRating, null, null,
                "title", null, null, null, null);
    }

    public List<Movie> getMoviesByYear(Integer year) {
        return filterMovies(null, null, null, year, null,
                "title", null, null, null, null);
    }

    public List<Movie> getMoviesTopNbyYear(Integer numberMovies) {
        if (numberMovies < 1) {
            throw new IllegalArgumentException("numberMovies must be 1 or more");
        }
        int pastYear = LocalDate.now().plusYears(-1).getYear();
        List<Movie> filteredMovies = filterMovies(null, null, null, pastYear, pastYear,
                "rating", null, null, null, null);
        return filteredMovies.stream().limit(numberMovies).toList();
    }

    public List<Movie> sortMovies(List<Movie> moviesToSort, String sort) {
        Stream<Movie> stream = moviesToSort.stream();
        if ("title".equalsIgnoreCase(sort)) {
            return stream
                    .sorted(java.util.Comparator.comparing(Movie::getTitle, String.CASE_INSENSITIVE_ORDER))
                    .toList();
        } else if ("year".equalsIgnoreCase(sort)) {
            return stream
                    .sorted(java.util.Comparator.comparingInt(Movie::getYear))
                    .toList();
        } else if ("rating".equalsIgnoreCase(sort)) {
            return stream
                    .sorted(java.util.Comparator.comparingDouble(Movie::getImdbRating).reversed())
                    .toList();
        }
        return stream.toList();
    }

    public List<Movie> filterMovies(String title, String genre, Double minRating, Integer yearStart, Integer yearEnd,
                                    String sort, String director, String actor, String oscarWon, String studio) {
        if (sort == null) {
            sort = "title";
        }
        if (yearEnd == null) {
            yearEnd = yearStart;
        }

        if (!Objects.equals(sort, "title") && !Objects.equals(sort, "year") && !Objects.equals(sort, "rating")) {
            throw new IllegalArgumentException("sort must be either title or year or rating");
        }
        int futureYear = LocalDate.now().plusYears(5).getYear(); // 5 years in the future
        if (yearStart != null && (yearStart < 1878 || yearStart > futureYear)) {
            throw new IllegalArgumentException(String.format("year start must be between 1878 and %s", futureYear));
        }
        if (yearEnd != null && (yearEnd < 1878 || yearEnd > futureYear)) {
            throw new IllegalArgumentException(String.format("year end must be between 1878 and %s", futureYear));
        }
        if (yearStart != null && yearEnd != null && yearEnd < yearStart ) {
            throw new IllegalArgumentException(String.format("year start %s must be before year end %s", yearStart, yearEnd));
        }

        final Integer finalYearEnd = yearEnd;
        List<Movie> filteredMovies = movies.stream()
                .filter(movie -> title == null     || movie.isTitle(title))
                .filter(movie -> genre == null     || movie.isGenre(genre))
                .filter(movie -> minRating == null || movie.getImdbRating() >= minRating)
                .filter(movie -> yearStart == null || movie.isInRange(yearStart, finalYearEnd))
                .filter(movie -> director == null  || movie.isDirector(director))
                .filter(movie -> actor == null     || movie.isActor(actor))
                .filter(movie -> oscarWon == null  || movie.isOscarsWonDetail(oscarWon))
                .filter(movie -> studio == null    || movie.isStudio(studio))
                .toList();
        return numberMovies(sortMovies(filteredMovies, sort));
    }

    public List<Movie> filterMovies(String title, String genre, Double minRating, Integer yearStart, Integer yearEnd) {
        return filterMovies(title, genre, minRating, yearStart, yearEnd, "title", null, null, null, null);
    }

    public List<Movie> numberMovies(List<Movie> movies) {
        if (movies == null) {
            return new ArrayList<>();
        }

        final java.util.concurrent.atomic.AtomicInteger idx = new java.util.concurrent.atomic.AtomicInteger(1);
        movies.stream()
                .filter(Objects::nonNull)
                .forEach(m -> m.setNum(idx.getAndIncrement()));
        return movies;
    }
}
