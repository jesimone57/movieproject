package com.example.demo.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MovieStudioTest {
    @Test
    void isProductionStudioTest() {
        // arrange
        Movie movie = new Movie();
        movie.setStudio("Paramount Pictures");

        // act & assert
        assertTrue(movie.isStudio("Paramount Pictures"));
        assertTrue(movie.isStudio("PARAMOUNT"));
        assertTrue(movie.isStudio("PARA"));
        assertTrue(movie.isStudio("PICT"));
        assertTrue(movie.isStudio(" pict "));
    }

    @Test
    void isStudioNullTest() {
        // arrange
        Movie movie = new Movie();

        // act & assert
        assertFalse(movie.isActor("Paramount Pictures"));
    }

    @Test
    void isStudioEmptyTest() {
        // arrange
        Movie movie = new Movie();
        movie.setStudio("");

        // act & assert
        assertFalse(movie.isActor("Paramount Pictures"));
    }

    @Test
    void isStudioBlankTest() {
        Movie movie = new Movie();
        movie.setStudio("        ");

        // act & assert
        assertFalse(movie.isActor("Paramount Pictures"));
    }
}
