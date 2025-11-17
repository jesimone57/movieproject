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

    private static final String TEST_RESOURCE = "movies-decade-of-1930s.json";
    private MovieService movieService;

    @BeforeEach
    void setUp() {
        movieService = new MovieService(TEST_RESOURCE);
    }

    @Test
    void getMovies() {
        List<Movie> movies = movieService.getAllMovies();
        assertNotNull(movies);
        assertFalse(movies.isEmpty());
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
        assertEquals(13, movies.size());
        for (Movie movie : movies) {
            assertTrue(StringUtils.containsIgnoreCase(movie.getTitle(), title));
        }
    }

    @Test
    void getMoviesByTitlePartialSearchSortedByTitle() {
        String title = "the";
        List<Movie> movies = movieService.getMovieByPartialTitle(title, "title");
        assertNotNull(movies);
        assertEquals(87, movies.size());
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
        assertEquals(87, movies.size());
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
        assertEquals(87, movies.size());
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
        assertEquals(29, movies.size());
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
        assertEquals(49, movies.size());
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
        assertEquals(49, movies.size());
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
        assertEquals(11, movies.size());
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
        assertEquals(24, movies.size());
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
        assertEquals(128, movies.size());
        for (Movie movie : movies) {
            assertTrue(movie.getImdbRating() >= imdb_rating);
        }
    }

    @Test
    void getMoviesByTitleAndYear() {
        String title = "love";
        Integer year = 1939;
        List<Movie> movies = movieService.filterMovies(title, null, null, year, null, "title", null, null);
        assertNotNull(movies);
        assertEquals(2, movies.size());
        for (Movie movie : movies) {
            assertTrue(StringUtils.containsIgnoreCase(movie.getTitle(), title));
            assertEquals(year, movie.getYear());
        }
    }

    @Test
    void getMoviesByGenreAndYear() {
        String genre = "Drama";
        Integer year = 1933;
        List<Movie> movies = movieService.filterMovies(null, genre, null, year, null);
        assertNotNull(movies);
        assertEquals(12, movies.size());
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
        assertEquals(4, movies.get(3).getNum());
        for (Movie movie : movies) {
            assertTrue(StringUtils.containsIgnoreCase(movie.getTitle(), title));
            assertTrue(movie.getGenres().contains(genre));
        }
    }

    @Test
    void getMoviesInRange() {
        Integer yearStart = 1930;
        Integer yearEnd = 1933;
        List<Movie> movies = movieService.filterMovies(null, null, null, yearStart, yearEnd, "year", null, null);
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
                movieService.filterMovies(null, null, null, yearStart, yearEnd, "year",null, null)
        );
    }

    @Test
    void getMoviesFilterByDirector() {
        String director = "hitchcock";
        List<Movie> movies = movieService.filterMovies(null, null, null, null, null, null, director, null);
        assertNotNull(movies);
        assertEquals(5, movies.size());
        for (Movie movie : movies) {
            assertTrue(StringUtils.containsIgnoreCase(movie.getDirector().getName(), director));
        }
    }

    @Test
    void getMoviesFilterByActor() {
        String actor = "gable";
        List<Movie> movies = movieService.filterMovies(null, null, null, null, null, null, null, actor);
        assertNotNull(movies);
        assertEquals(8, movies.size());
        for (Movie movie : movies) {
            assertTrue(movie.isActor(actor));
        }
    }

    @Test
    void getMoviesFilterByActorCompound() {
        String actor = "gable, colb";
        List<Movie> movies = movieService.filterMovies(null, null, null, null, null, null, null, actor);
        assertNotNull(movies);
        assertEquals(1, movies.size());
        assertEquals("It Happened One Night", movies.getFirst().getTitle());
        for (Movie movie : movies) {
            assertTrue(movie.isActor(actor));
        }
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
