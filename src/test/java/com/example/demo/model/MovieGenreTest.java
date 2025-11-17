package com.example.demo.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

public class MovieGenreTest {
    @Test
    void isGenreTest() {
        // arrange
        Movie movie = new Movie();
        List<String> genres = movie.getGenres();
        genres.add("Drama");
        genres.add("Comedy");

        // act & assert
        assertTrue(movie.isGenre("Drama"));
        assertTrue(movie.isGenre("Dram"));
        assertTrue(movie.isGenre("com"));
    }

    @Test
    void isGenreFromEmptyList() {
        // arrange
        Movie movie = new Movie();

        // act & assert
        assertFalse(movie.isGenre("Drama"));
    }

    @Test
    void isGenreNull() {
        // arrange
        Movie movie = new Movie();
        movie.getGenres().add(null);

        // act & assert
        assertFalse(movie.isGenre("Drama"));
    }

    @Test
    void isGenreEmpty() {
        // arrange
        Movie movie = new Movie();
        movie.getGenres().add("");

        // act & assert
        assertFalse(movie.isGenre("Drama"));
    }

    @Test
    void isGenreBlank() {
        // arrange
        Movie movie = new Movie();
        movie.getGenres().add("     ");

        // act & assert
        assertFalse(movie.isGenre("Drama"));
    }

    @Test
    void isGenreFromBComoundNames() {
        // arrange
        Movie movie = new Movie();
        List<String> genres = movie.getGenres();
        genres.add("Drama");
        genres.add("Comedy");

        // act & assert
        assertTrue(movie.isGenre("dram, com"));
        assertTrue(movie.isGenre("com, dram"));

        assertFalse(movie.isGenre("dram  com")); // missing delimiter
        assertFalse(movie.isGenre("com, dram, west")); // genre not found
    }

    @Test
    void isGenreFromBComoundNamesWithCaseAndWhitespace() {
        // arrange
        Movie movie = new Movie();
        List<String> genres = movie.getGenres();
        genres.add("Drama");
        genres.add("Comedy");

        // act & assert
        assertTrue(movie.isGenre(" DRAM , COM "));
        assertTrue(movie.isGenre(" com , dram  "));

        assertFalse(movie.isGenre(" DRAM   COM ")); // missing delimiter
        assertFalse(movie.isGenre(" com ,  dram , west ")); // genre not found
    }

}
