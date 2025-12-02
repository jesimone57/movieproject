package com.example.demo.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ActorMovieTest {

    @Test
    void getPlotByText() {
        ActorMovie actorMovie = new ActorMovie();
        actorMovie.setPlot(" a man with a plan!");
        assertTrue(actorMovie.isPlotText("man"));
        assertTrue(actorMovie.isPlotText("MAN"));
        assertTrue(actorMovie.isPlotText(" Man   "));
        assertTrue(actorMovie.isPlotText(" Man WITH a"));
        assertFalse(actorMovie.isPlotText("asdf"));
    }

    @Test
    void getPlotByTextNull() {
        ActorMovie actorMovie = new ActorMovie();
        assertFalse(actorMovie.isPlotText(null));
        assertFalse(actorMovie.isPlotText(""));
        assertFalse(actorMovie.isPlotText("   "));
        assertFalse(actorMovie.isPlotText("asdf"));
    }

    @Test
    void getPlotByTextBlank() {
        ActorMovie actorMovie = new ActorMovie();
        actorMovie.setPlot("   ");
        assertFalse(actorMovie.isPlotText(null));
        assertFalse(actorMovie.isPlotText(""));
        assertFalse(actorMovie.isPlotText("   "));
        assertFalse(actorMovie.isPlotText("asdf"));
    }

    @Test
    void getRoleByText() {
        ActorMovie actorMovie = new ActorMovie();
        actorMovie.setRole(" plays the hideous monster gorgon");
        assertTrue(actorMovie.isRoleText("hid"));
        assertTrue(actorMovie.isRoleText("HIDE"));
        assertTrue(actorMovie.isRoleText(" ster   "));
        assertTrue(actorMovie.isRoleText(" STER"));
        assertFalse(actorMovie.isRoleText("asdf"));
    }

    @Test
    void getRoleByTextNull() {
        ActorMovie actorMovie = new ActorMovie();
        assertFalse(actorMovie.isRoleText(null));
        assertFalse(actorMovie.isRoleText(""));
        assertFalse(actorMovie.isRoleText("   "));
        assertFalse(actorMovie.isRoleText("asdf"));
    }

    @Test
    void getRoleByTextBlank() {
        ActorMovie actorMovie = new ActorMovie();
        actorMovie.setRole("   ");
        assertFalse(actorMovie.isRoleText(null));
        assertFalse(actorMovie.isRoleText(""));
        assertFalse(actorMovie.isRoleText("   "));
        assertFalse(actorMovie.isRoleText("asdf"));
    }


    @Test
    void searchFilmographyDisplayText() {
        ActorMovie actorMovie = new ActorMovie();
        actorMovie.setTitle("I love hippos");
        actorMovie.setYear(2018);
        actorMovie.setPlot("a man with a plan!");
        actorMovie.setAwards("bafta won");
        actorMovie.getRatings().setImdb(6.7);

        assertTrue(actorMovie.isTextInCurrentFilmographyDisplayText("Hippo"));
        assertTrue(actorMovie.isTextInCurrentFilmographyDisplayText("   2018"));
        assertTrue(actorMovie.isTextInCurrentFilmographyDisplayText("Man"));
        assertTrue(actorMovie.isTextInCurrentFilmographyDisplayText("Won"));
        assertTrue(actorMovie.isTextInCurrentFilmographyDisplayText("baf"));
        assertTrue(actorMovie.isTextInCurrentFilmographyDisplayText("6.7"));

        assertFalse(actorMovie.isTextInCurrentFilmographyDisplayText("asdf"));
    }
}
