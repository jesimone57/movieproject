package com.example.demo.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MovieDirectorTest {
    @Test
    void isDirectorTest() {
        // arrange
        Movie movie = new Movie();
        Director director = new Director();
        director.setName("Hitchcock");
        movie.setDirector(director);

        // act & assert
        assertTrue(movie.isDirector("hitchcock"));
        assertTrue(movie.isDirector("HITCH"));
        assertTrue(movie.isDirector(" hitch  "));
        assertTrue(movie.isDirector(" cock  "));
    }

    @Test
    void isDirectorNullTest() {
        // arrange
        Movie movie = new Movie();

        // act & assert
        assertFalse(movie.isDirector("Hitchcock"));
    }

    @Test
    void isDirectorNullNameTest() {
        // arrange
        Movie movie = new Movie();
        Director director = new Director();
        director.setName(null);
        movie.setDirector(director);

        // act & assert
        assertFalse(movie.isDirector("Hitchcock"));
    }

    @Test
    void isDirectorEmptyNameTest() {
        // arrange
        Movie movie = new Movie();
        Director director = new Director();
        director.setName("");
        movie.setDirector(director);

        // act & assert
        assertFalse(movie.isDirector("Hitchcock"));
    }

    @Test
    void isDirectorBlankNameTest() {
        // arrange
        Movie movie = new Movie();
        Director director = new Director();
        director.setName("     ");
        movie.setDirector(director);

        // act & assert
        assertFalse(movie.isDirector("Hitchcock"));
    }
}
