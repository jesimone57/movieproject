package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.demo.model.ActorFilmography;
import com.example.demo.service.ActorFilmographyService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActorFilmographiesTest {

    private static final String TEST_RESOURCE = "actor-movies-sample.json";
    private ActorFilmographyService actorFilmographyServiceService;

    @BeforeEach
    void setUp() {
        actorFilmographyServiceService = new ActorFilmographyService(TEST_RESOURCE);
    }

    @Test
    void getFilmographies() {
        List<ActorFilmography> filmographies = actorFilmographyServiceService.filterActorFilmographies(null,
                null, null, null);
        assertNotNull(filmographies);
        assertFalse(filmographies.isEmpty());
    }

    @Test
    void getActorByName() {
        String name = "streep";
        List<ActorFilmography> filmographies = actorFilmographyServiceService.filterActorFilmographies(name,
                null, null, null);
        assertNotNull(filmographies);
        assertFalse(filmographies.isEmpty());
        assertEquals(1, filmographies.size());
        assertEquals("Meryl Streep", filmographies.getFirst().getActorProfile().getName());
    }

    @Test
    void getActorByPartialName() {
        String name = "e";
        List<ActorFilmography> filmographies = actorFilmographyServiceService.filterActorFilmographies(name,
                null, null, null);
        assertNotNull(filmographies);
        assertFalse(filmographies.isEmpty());
        assertEquals(2, filmographies.size());
        assertEquals("Katharine Hepburn", filmographies.getFirst().getActorProfile().getName());
        assertEquals("Meryl Streep", filmographies.getLast().getActorProfile().getName());
    }

    @Test
    void getActorByPartialNameWhitespace() {
        String name = " rlo  ";
        List<ActorFilmography> filmographies = actorFilmographyServiceService.filterActorFilmographies(name,
                null, null, null);
        assertNotNull(filmographies);
        assertFalse(filmographies.isEmpty());
        assertEquals(1, filmographies.size());
        assertEquals("Marlon Brando", filmographies.getFirst().getActorProfile().getName());
    }

    @Test
    void getActorByYear() {
        Integer year = 1932;
        List<ActorFilmography> filmographies = actorFilmographyServiceService.filterActorFilmographies(null,
                year, null, null);
        assertNotNull(filmographies);
        assertFalse(filmographies.isEmpty());
        assertEquals(2, filmographies.size());
        for (ActorFilmography af : filmographies) {
            assertTrue(af.getActorProfile().isInRange(year));
        }
    }
    /*

    @Test
    void getMoviesByTitlePartialSearchSortedByYear() {
        String title = "the";
        List<Movie> movies = movieService.getMovieByPartialTitle(title, "year");
        assertNotNull(movies);
        assertEquals(76, movies.size());
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
        assertEquals(76, movies.size());
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
        assertEquals(25, movies.size());
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
        assertEquals(42, movies.size());
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
        assertEquals(42, movies.size());
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
        assertEquals(9, movies.size());
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
        double imdbRating = 7.4;
        List<Movie> movies = movieService.getMovieByRating(imdbRating);
        assertNotNull(movies);
        assertEquals(112, movies.size());
        for (Movie movie : movies) {
            assertTrue(movie.getImdbRating() >= imdbRating);
        }
    }

    @Test
    void getMoviesByTitleAndYear() {
        String title = "love";
        Integer year = 1939;
        List<Movie> movies = movieService.filterMovies(title, null, null, year, null, "title", null, null, null, null);
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
        assertEquals(11, movies.size());
        for (Movie movie : movies) {
            assertTrue(movie.getGenres().contains(genre));
            assertEquals(year, movie.getYear());
        }
    }

    @Test
    void getMoviesByAllFilters() {
        String title = "the";
        String genre = "Crime";
        double minRating = 7.4;
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
        int yearStart = 1930;
        int yearEnd = 1933;
        List<Movie> movies = movieService.filterMovies(null, null, null, yearStart, yearEnd,
                "year", null, null, null, null);
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
                movieService.filterMovies(null, null, null, yearStart, yearEnd,
                        "year",null, null, null, null)
        );
    }

    @Test
    void getMoviesFilterByDirector() {
        String director = "hitchcock";
        List<Movie> movies = movieService.filterMovies(null, null, null, null, null,
                null, director, null, null, null);
        assertNotNull(movies);
        assertEquals(5, movies.size());
        for (Movie movie : movies) {
            assertTrue(movie.isDirector(director));
        }
    }

    @Test
    void getMoviesFilterByDirectorWhitespace() {
        String director = "  hitchcock    ";
        List<Movie> movies = movieService.filterMovies(null, null, null, null, null,
                null, director, null, null, null);
        assertNotNull(movies);
        assertEquals(5, movies.size());
        for (Movie movie : movies) {
            assertTrue(movie.isDirector(director));
        }
    }

    @Test
    void getMoviesFilterByActor() {
        String actor = "gable";
        List<Movie> movies = movieService.filterMovies(null, null, null, null, null,
                null, null, actor, null, null);
        assertNotNull(movies);
        assertEquals(7, movies.size());
        for (Movie movie : movies) {
            assertTrue(movie.isActor(actor));
        }
    }

    @Test
    void getMoviesFilterByActorCompound() {
        String actor = "gable, colb";
        List<Movie> movies = movieService.filterMovies(null, null, null, null, null,
                null, null, actor, null, null);
        assertNotNull(movies);
        assertEquals(1, movies.size());
        assertEquals("It Happened One Night", movies.getFirst().getTitle());
        for (Movie movie : movies) {
            assertTrue(movie.isActor(actor));
        }
    }

    @Test
    void getMoviesFilterByOscarsWonDetails() {
        String oscarsWonDetail = "picture";
        List<Movie> movies = movieService.filterMovies(null, null, null, null, null,
                null, null, null, oscarsWonDetail, null);
        assertNotNull(movies);
        assertEquals(10, movies.size());
        for (Movie movie : movies) {
            assertTrue(movie.isOscarsWonDetail(oscarsWonDetail));
        }
    }

    @Test
    void getMoviesFilterByOscarsWonDetailsCompound() {
        String oscarsWonDetail = "picture, actress";
        List<Movie> movies = movieService.filterMovies(null, null, null, null, null,
                null, null, null, oscarsWonDetail, null);
        assertNotNull(movies);
        assertEquals(3, movies.size());
        assertEquals("Gone with the Wind", movies.getFirst().getTitle());
        for (Movie movie : movies) {
            assertTrue(movie.isOscarsWonDetail(oscarsWonDetail));
        }
    }

    @Test
    void getMoviesFilterByStudio() {
        String studio = "paramount";
        List<Movie> movies = movieService.filterMovies(null, null, null, null, null,
                null, null, null, null, studio);
        assertNotNull(movies);
        assertEquals(28, movies.size());
        for (Movie movie : movies) {
            assertTrue(movie.isStudio(studio));
        }
    }

    @Test
    void getMoviesFilterByStudioWhitespace() {
        String studio = "   paramount   ";
        List<Movie> movies = movieService.filterMovies(null, null, null, null, null,
                null, null, null, null, studio);
        assertNotNull(movies);
        assertEquals(28, movies.size());
        for (Movie movie : movies) {
            assertTrue(movie.isStudio(studio));
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
     */
}
