package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.demo.model.Movie;
import com.example.demo.service.MovieService;
import java.time.LocalDate;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DemoApplicationUnitTests {

    private static final String TEST_RESOURCE = "movies-1930.json";
    private MovieService movieService;

    @BeforeEach
    void setUp() {
        movieService = new MovieService(TEST_RESOURCE);
    }

    @Test
    void getMovies() {
        assertNotNull(movieService.getAllMovies());
        assertFalse(movieService.getAllMovies().isEmpty());
    }

    @Test
    void getMovieByTitle() {
        String title = "Scarface";
        List<Movie> movies = movieService.getMovieByPartialTitle(title, null);
        assertNotNull(movies);
        assertFalse(movies.isEmpty());
        assertEquals(1, movies.size());
        assertEquals(title, movies.getFirst().getTitle());
    }

    @Test
    void getDuplicateMoviesByTitle() {
        String title = "mum";
        List<Movie> movies = movieService.getMovieByPartialTitle(title, null);
        assertNotNull(movies);
        assertEquals(1, movies.size());
        for (Movie movie : movies) {
            assertTrue(movie.getTitle().compareToIgnoreCase(title) > 0);
        }
    }

    @Test
    void getMoviesByTitlePartialSearch() {
        String title = "ea";
        List<Movie> movies = movieService.getMovieByPartialTitle(title, null);
        assertNotNull(movies);
        assertEquals(2, movies.size());
        for (Movie movie : movies) {
            assertTrue(movie.getTitle().compareToIgnoreCase(title) > 0);
        }
    }

    @Test
    void getMoviesByTitlePartialSearchSortedByTitle() {
        String title = "the";
        List<Movie> movies = movieService.getMovieByPartialTitle(title, "title");
        assertNotNull(movies);
        assertEquals(31, movies.size());
        for (int i = 0; i < movies.size() - 1; i++) {
            String currentTitle = movies.get(i).getTitle();
            String nextTitle = movies.get(i + 1).getTitle();
            assertTrue(currentTitle.compareToIgnoreCase(nextTitle) <= 0);
        }
    }

    @Test
    void getMoviesByTitlePartialSearchSortedByYear() {
        String title = "the";
        List<Movie> movies = movieService.getMovieByPartialTitle(title, "year");
        assertNotNull(movies);
        assertEquals(31, movies.size());
        for (int i = 0; i < movies.size() - 1; i++) {
            int currentYear = movies.get(i).getYear();
            int nextYear = movies.get(i + 1).getYear();
            assertTrue(currentYear <= nextYear);
        }
    }

    @Test
    void getMoviesByTitlePartialSearchSortedByRating() {
        String title = "the";
        List<Movie> movies = movieService.getMovieByPartialTitle(title, "rating");
        assertNotNull(movies);
        assertEquals(31, movies.size());
        for (int i = 0; i < movies.size() - 1; i++) {
            double currentRating = movies.get(i).getImdbRating();
            double nextRating = movies.get(i + 1).getImdbRating();
            assertTrue(nextRating <= currentRating);
        }
    }

    @Test
    void getMoviesByTitlePartialSearchSortedByInvalidField() {
        String title = "the";
        assertThrows(
                IllegalArgumentException.class, () -> movieService.getMovieByPartialTitle(title, "fake")
        );
    }

    @Test
    void checkByGenre() {
        String genre = "Crime";
        List<Movie> movies = movieService.getMovieByGenre(genre);
        assertNotNull(movies);
        assertEquals(10, movies.size());
        for (Movie movie : movies) {
            List<String> genres = movie.getGenres();
            assertTrue(genres.contains(genre));
        }
    }

    @Test
    void checkByGenreDoesntExists() {
        String genre = "Fake";
        List<Movie> movies = movieService.getMovieByGenre(genre);
        assertNotNull(movies);
        assertTrue(movies.isEmpty());
    }

    @Test
    void checkByCompoundGenres() {
        String genre = "Romance,Comedy";
        List<Movie> movies = movieService.getMovieByGenre(genre);
        assertNotNull(movies);
        assertEquals(17, movies.size());
        for (Movie movie : movies) {
            List<String> genres = movie.getGenres();
            assertTrue(genres.contains("Romance"));
            assertTrue(genres.contains("Comedy"));
        }
        assertEquals("You Can't Take It with You", movies.getLast().getTitle());
    }

    @Test
    void checkByCompoundGenresWithSpaces() {
        String genre = "  Romance , Comedy  ";
        List<Movie> movies = movieService.getMovieByGenre(genre);
        assertNotNull(movies);
        assertEquals(17, movies.size());
        for (Movie movie : movies) {
            List<String> genres = movie.getGenres();
            assertTrue(genres.contains("Romance"));
            assertTrue(genres.contains("Comedy"));
        }
        assertEquals("You Can't Take It with You", movies.getLast().getTitle());
    }

    @Test
    void checkByCompoundGenres3() {
        String genre = "  Drama , Film-Noir , Crime ";
        List<Movie> movies = movieService.getMovieByGenre(genre);
        assertNotNull(movies);
        assertEquals(3, movies.size());
        for (Movie movie : movies) {
            List<String> genres = movie.getGenres();
            assertTrue(genres.contains("Crime"));
            assertTrue(genres.contains("Film-Noir"));
            assertTrue(genres.contains("Drama"));
        }
        assertEquals("The Roaring Twenties", movies.getLast().getTitle());
    }

    @Test
    void checkByNullGenre() {
        List<Movie> movies = movieService.getMovieByGenre(null);
        assertNotNull(movies);
        assertTrue(movies.isEmpty());
    }

    @Test
    void checkByEmptyStringGenre() {
        List<Movie> movies = movieService.getMovieByGenre("");
        assertNotNull(movies);
        assertTrue(movies.isEmpty());
    }

    @Test
    void checkByBlankGenre() {
        List<Movie> movies = movieService.getMovieByGenre("           ");
        assertNotNull(movies);
        assertTrue(movies.isEmpty());
    }

    @Test
    void checkByBlankCompoundGenres() {
        List<Movie> movies = movieService.getMovieByGenre("   ,     ,   ");
        assertNotNull(movies);
        assertTrue(movies.isEmpty());
    }

    @Test
    void checkByYear() {
        int year = 1935;
        List<Movie> movies = movieService.getMoviesByYear(year);
        assertNotNull(movies);
        assertEquals(9, movies.size());
        for (Movie movie : movies) {
            assertEquals(year, movie.getYear());
        }
    }

    @Test
    void checkByYearOutOfBounds() {
        int year = 1800;
        assertThrows(
                IllegalArgumentException.class, () -> movieService.getMoviesByYear(year)
        );
    }

    @Test
    void checkByYearDoesNotExist() {
        int year = 1965;
        List<Movie> movies = movieService.getMoviesByYear(year);
        assertNotNull(movies);
        assertTrue(movies.isEmpty());
    }

    @Test
    void checkByYearNegative() {
        int year = -1994;
        assertThrows(
                IllegalArgumentException.class, () -> movieService.getMoviesByYear(year)
        );
    }

    @Test
    void checkByYearInTheFuture() {
        int year = LocalDate.now().plusYears(5).getYear();
        List<Movie> movies = movieService.getMoviesByYear(year);
        assertNotNull(movies);
        assertTrue(movies.isEmpty());
    }

    @Test
    void checkByRating() {
        double imdb_rating = 7.4;
        List<Movie> movies = movieService.getMovieByRating(imdb_rating);
        assertNotNull(movies);
        assertEquals(69, movies.size());
        for (Movie movie : movies) {
            assertTrue(movie.getImdbRating() >= imdb_rating);
        }
    }

    @Test
    void getMoviesByTitleAndYear() {
        String title = "arm";
        Integer year = 1932;
        List<Movie> movies = movieService.filterMovies(title, null, null, year, null, "title");
        assertNotNull(movies);
        assertEquals(1, movies.size());
        for (Movie movie : movies) {
            assertTrue(StringUtils.containsIgnoreCase(movie.getTitle(), title));
            assertEquals(year, movie.getYear());
        }
    }

    @Test
    void getMoviesByGenreAndYear() {
        String genre = "Comedy";
        Integer year = 1933;
        List<Movie> movies = movieService.filterMovies(null, genre, null, year, null);
        assertNotNull(movies);
        assertEquals(4, movies.size());
        for (Movie movie : movies) {
            assertTrue(movie.getGenres().contains(genre));
            assertEquals(year, movie.getYear());
        }
    }

    @Test
    void getMoviesByAllFilters() {
        String title = "the";
        String genre = "Crime";
        Double minRating = 7.4;
        Integer year = 1934;
        List<Movie> movies = movieService.filterMovies(title, genre, minRating, year, null);
        assertNotNull(movies);
        assertEquals(2, movies.size());
        for (Movie movie : movies) {
            assertTrue(StringUtils.containsIgnoreCase(movie.getTitle(), title));
            assertTrue(movie.getGenres().contains(genre));
            assertTrue(movie.getImdbRating() >= minRating);
            assertEquals(year, movie.getYear());
        }
    }

    @Test
    void getNumberedMovies() {
        String title = "the";
        String genre = "Drama";
        List<Movie> movies = movieService.filterMovies(title, genre, null, null, null);
        assertEquals(15, movies.get(14).getNum());
        for (Movie movie : movies) {
            assertTrue(StringUtils.containsIgnoreCase(movie.getTitle(), title));
            assertTrue(movie.getGenres().contains(genre));
        }
    }

    @Test
    void getMoviesInRange() {
        Integer yearStart = 1930;
        Integer yearEnd = 1933;
        List<Movie> movies = movieService.filterMovies(null, null, null, yearStart, yearEnd, "year");
        assertEquals(28, movies.get(27).getNum());
        for (Movie movie : movies) {
            assertTrue(movie.getYear() >= yearStart);
            assertTrue(movie.getYear() <= yearEnd);
        }
    }

    @Test
    void getMoviesInvalidDateRange() {
        Integer yearStart = 1995;
        Integer yearEnd = 1990;
        assertThrows(IllegalArgumentException.class, () ->
                movieService.filterMovies(null, null, null, yearStart, yearEnd, "year")
        );
    }

    //@Test
    void getHighestRatedMoviesFromPastYearExactMatch() {
        List<Movie> movies = movieService.getMoviesTopNbyYear(2);
        assertEquals(2, movies.size());
    }

    //@Test
    void getHighestRatedMoviesFromPastYearLessThanRequested() {
        List<Movie> movies = movieService.getMoviesTopNbyYear(50);
        assertEquals(2, movies.size());
    }

    //@Test
    void getHighestRatedMoviesFromPastYearNotMoreThanRequested() {
        List<Movie> movies = movieService.getMoviesTopNbyYear(1);
        assertEquals(1, movies.size());
    }

    @Test
    void getHighestRatedMoviesFromPastYearInvalidLimit() {
        assertThrows(IllegalArgumentException.class, () ->
                movieService.getMoviesTopNbyYear(0)
        );
    }

    @Test
    void getHighestRatedMoviesFromPastYearInvalidNegativeLimit() {
        assertThrows(IllegalArgumentException.class, () ->
                movieService.getMoviesTopNbyYear(-1)
        );
    }
}
