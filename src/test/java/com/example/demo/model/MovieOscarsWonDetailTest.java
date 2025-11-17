package com.example.demo.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class MovieOscarsWonDetailTest {
    @Test
    void isOscarsWonDetailTest() {
        // arrange
        Movie movie = new Movie();
        List<String> oscarsWonDetails = movie.getOscarsWonDetails();
        oscarsWonDetails.add("Best Picture");
        oscarsWonDetails.add("Best Actress");

        // act & assert
        assertTrue(movie.isOscarsWonDetail("Picture"));
        assertTrue(movie.isOscarsWonDetail("best picture"));
    }

    @Test
    void isOscarsWonDetailFromEmptyList() {
        // arrange
        Movie movie = new Movie();

        // act & assert
        assertFalse(movie.isOscarsWonDetail("Best Picture"));
    }

    @Test
    void isOscarsWonDetailNull() {
        // arrange
        Movie movie = new Movie();
        movie.getOscarsWonDetails().add(null);

        // act & assert
        assertFalse(movie.isOscarsWonDetail("Best Picture"));
    }

    @Test
    void isOscarsWonDetailEmpty() {
        // arrange
        Movie movie = new Movie();
        movie.getOscarsWonDetails().add("");

        // act & assert
        assertFalse(movie.isOscarsWonDetail("Best Picture"));
    }

    @Test
    void isOscarsWonDetailBlank() {
        // arrange
        Movie movie = new Movie();
        movie.getOscarsWonDetails().add("     ");

        // act & assert
        assertFalse(movie.isOscarsWonDetail("Best Picture"));
    }

    @Test
    void isOscarsWonDetailFromBComoundNames() {
        // arrange
        Movie movie = new Movie();
        List<String> oscarsWonDetails = movie.getOscarsWonDetails();
        oscarsWonDetails.add("Best Picture");
        oscarsWonDetails.add("Best Actress");

        // act & assert
        assertTrue(movie.isOscarsWonDetail("best picture, best actress"));
        assertTrue(movie.isOscarsWonDetail("picture, actress"));

        assertFalse(movie.isOscarsWonDetail("picture actress")); // missing delimiter
        assertFalse(movie.isOscarsWonDetail("picture, actress, screenplay")); // OscarWonDetail not found
    }

    @Test
    void isOscarsWonDetailFromComoundNamesWithCaseAndWhitespace() {
        // arrange
        Movie movie = new Movie();
        List<String> oscarsWonDetails = movie.getOscarsWonDetails();
        oscarsWonDetails.add("Best Picture");
        oscarsWonDetails.add("Best Actress");

        // act & assert
        assertTrue(movie.isOscarsWonDetail(" BEST PICTURE , BEST ACTRESS "));
        assertTrue(movie.isOscarsWonDetail(" picture , act  "));

        assertFalse(movie.isOscarsWonDetail(" BEST PICTURE  BEST ACTRESS ")); // missing delimiter
        assertFalse(movie.isOscarsWonDetail(" picture ,  actress , screenplay ")); // OscarWonDetail not found
    }

}
