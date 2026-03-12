package com.example.demo.service;

import com.example.demo.model.Movie;
import com.example.demo.model.MovieFilter;
import com.example.demo.util.JsonResourceLoader;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);
    private static final String DEFAULT_SORT = "rating";
    private final List<Movie> movies;
    private static final String RESOURCE_FILE = "movies-by-year/movies-1958.json";
    private static final String MOVIES_FOLDER = "movies-by-year";

    @Autowired
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
                throw new IOException("Could not find " + resourceFile);
            }

            return objectMapper.readValue(inputStream, new TypeReference<ArrayList<Movie>>() {});
        } catch (IOException e) {
            logger.error("Failed to load movies from {}", resourceFile, e);
            return new ArrayList<>();
        }
    }

    /** Convenience method: loads from the default movies folder on the classpath. */
    public void loadMoviesFromFolder() {
        loadMoviesFromFolder(MOVIES_FOLDER);
    }

    /**
     * Scans {@code classpath*:<folder>/**&#47;*.json} and appends all parsed movies.
     * Uses {@link JsonResourceLoader} — tries single-object then array per file.
     * Malformed files are skipped.
     */
    public void loadMoviesFromFolder(String folder) {
        if (folder == null || StringUtils.isBlank(folder)) {
            throw new IllegalArgumentException("folder cannot be null or empty");
        }
        logger.info("Loading Movies from resource folder: {}", folder);
        List<Movie> loaded = JsonResourceLoader.loadFromFolder(
                folder, Movie.class, new ObjectMapper(), this::removeDuplicateMovies, logger);
        this.movies.addAll(loaded);
        logger.info("Total movies in memory: {}", this.movies.size());
    }

    public List<Movie> getAllMovies() {
        return filterMovies(MovieFilter.builder().build());
    }

    public List<String> getAllGenres() {
        // Use streams and lambdas to flatten all movie genres, remove blanks, de-duplicate, and sort
        return this.movies.stream()
                .filter(Objects::nonNull)
                .flatMap(m -> {
                    List<String> genres = m.getGenres();
                    return genres == null ? Stream.empty() : genres.stream();
                })
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .sorted()
                .toList();
    }

    public List<Movie> getMovieByPartialTitle(String title, String sort) {
        return filterMovies(MovieFilter.builder()
                .title(title)
                .sort(sort != null ? sort : MovieFilter.DEFAULT_SORT)
                .build());
    }

    public List<Movie> removeDuplicateMovies(List<Movie> movies) {
        Set<Movie> uniqueMovies = new HashSet<>(movies);
        return new ArrayList<>(uniqueMovies);
    }

    public Map<String, Integer> findDuplicateTitles() {
        Map<String, Integer> titleCounts = new HashMap<>();
        Map<String, Integer> duplicates = new HashMap<>();
        for (Movie movie : movies) {
            String title = movie.getTitle();
            titleCounts.put(title, titleCounts.getOrDefault(title, 0) + 1);
        }
        for (Map.Entry<String, Integer> entry : titleCounts.entrySet()) {
            if (entry.getValue() > 1) {
                duplicates.put(entry.getKey(), entry.getValue());
            }
        }
        return duplicates;
    }

    public boolean hasDuplicateTitles() {
        return !findDuplicateTitles().isEmpty();
    }

    public List<Movie> getMovieByGenre(String genre) {
        if (StringUtils.isBlank(genre)) {
            return new ArrayList<>();
        }
        return filterMovies(MovieFilter.builder().genre(genre).build());
    }

    public List<Movie> getMovieByRating(Double imdbRating) {
        return filterMovies(MovieFilter.builder().minRating(imdbRating).build());
    }

    public List<Movie> getMoviesByYear(Integer year) {
        return filterMovies(MovieFilter.builder().yearStart(year).yearEnd(year).build());
    }

    public List<Movie> getMoviesTopNbyYear(Integer numberMovies) {
        if (numberMovies < 1) {
            throw new IllegalArgumentException("numberMovies must be 1 or more");
        }
        int pastYear = LocalDate.now().plusYears(-1).getYear();
        List<Movie> filteredMovies = filterMovies(MovieFilter.builder()
                .yearStart(pastYear).yearEnd(pastYear).sort(DEFAULT_SORT).build());
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

    /**
     * Primary filter method — accepts a {@link MovieFilter} value object.
     * All other {@code filterMovies} overloads delegate here.
     * (Complex validation logic needed for year range, sort validation)
     */
    @SuppressWarnings("squid:S3776")  // Ignore cognitive complexity warning - validation requires it
    public List<Movie> filterMovies(MovieFilter f) {
        String sort = (f.sort() == null) ? "title" : f.sort();

        if (!Objects.equals(sort, "title") && !Objects.equals(sort, "year") && !Objects.equals(sort, DEFAULT_SORT)) {
            throw new IllegalArgumentException("sort must be either title or year or rating");
        }

        Integer yearStart = f.yearStart();
        Integer yearEnd = (f.yearEnd() == null) ? yearStart : f.yearEnd();

        int futureYear = LocalDate.now().plusYears(5).getYear();
        if (yearStart != null && (yearStart < 1878 || yearStart > futureYear)) {
            throw new IllegalArgumentException(String.format("year start must be between 1878 and %s", futureYear));
        }
        if (yearEnd != null && (yearEnd < 1878 || yearEnd > futureYear)) {
            throw new IllegalArgumentException(String.format("year end must be between 1878 and %s", futureYear));
        }
        if (yearStart != null && yearEnd != null && yearEnd < yearStart) {
            throw new IllegalArgumentException(
                    String.format("year start %s must be before year end %s", yearStart, yearEnd));
        }

        final Integer finalYearEnd = yearEnd;
        List<Movie> filtered = movies.stream()
                .filter(movie -> f.title()     == null || movie.isTitle(f.title()))
                .filter(movie -> f.genre()     == null || movie.isGenre(f.genre()))
                .filter(movie -> f.minRating() == null || movie.getImdbRating() >= f.minRating())
                .filter(movie -> yearStart     == null || movie.isInRange(yearStart, finalYearEnd))
                .filter(movie -> f.director()  == null || movie.isDirector(f.director()))
                .filter(movie -> f.actor()     == null || movie.isActor(f.actor()))
                .filter(movie -> f.oscarWon()  == null || movie.isOscarsWonDetail(f.oscarWon()))
                .filter(movie -> f.studio()    == null || movie.isStudio(f.studio()))
                .toList();

        return numberMovies(sortMovies(filtered, sort));
    }

    /**
     * Backward-compatible 10-parameter overload — delegates to {@link #filterMovies(MovieFilter)}.
     * (Kept for legacy API compatibility)
     */
    @SuppressWarnings("squid:S107")  // Ignore method parameter count warning - legacy compatibility
    public List<Movie> filterMovies(String title, String genre, Double minRating,
                                    Integer yearStart, Integer yearEnd, String sort,
                                    String director, String actor, String oscarWon, String studio) {
        return filterMovies(new MovieFilter(title, genre, minRating, yearStart, yearEnd,
                sort, director, actor, oscarWon, studio));
    }

    /**
     * Convenience 5-parameter overload — delegates to {@link #filterMovies(MovieFilter)}.
     */
    public List<Movie> filterMovies(String title, String genre, Double minRating,
                                    Integer yearStart, Integer yearEnd) {
        return filterMovies(MovieFilter.builder()
                .title(title).genre(genre).minRating(minRating)
                .yearStart(yearStart).yearEnd(yearEnd)
                .build());
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
