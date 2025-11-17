package com.example.demo.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class MovieActorTest {
    @Test
    void isActorTest() {
        // arrange
        Movie movie = new Movie();
        List<Actor> actors = movie.getActors();
        Actor actor1 = new Actor();
        actor1.setName("Fred Brown");
        actors.add(actor1);
        Actor actor2 = new Actor();
        actor2.setName("Oscar Brown");
        actors.add(actor2);

        // act & assert
        assertTrue(movie.isActor("Fred Brown"));
        assertTrue(movie.isActor("Oscar Brown"));
        assertTrue(movie.isActor("oscar"));
        assertTrue(movie.isActor("fred"));
        assertTrue(movie.isActor("brown"));
    }

    @Test
    void isActorFromEmptyList() {
        // arrange
        Movie movie = new Movie();

        // act & assert
        assertFalse(movie.isActor("Fred Brown"));
    }

    @Test
    void isActorFromNullActorName() {
        // arrange
        Movie movie = new Movie();
        movie.getActors().add(new Actor());

        // act & assert
        assertFalse(movie.isActor("Fred Brown"));
    }

    @Test
    void isActorFromEmptyActorName() {
        // arrange
        Movie movie = new Movie();
        Actor actor1 = new Actor();
        actor1.setName("");
        movie.getActors().add(actor1);

        // act & assert
        assertFalse(movie.isActor("Fred Brown"));
    }

    @Test
    void isActorFromBlankActorName() {
        // arrange
        Movie movie = new Movie();
        Actor actor1 = new Actor();
        actor1.setName("      ");
        movie.getActors().add(actor1);

        // act & assert
        assertFalse(movie.isActor("Fred Brown"));
    }

    @Test
    void isActorFromBComoundNames() {
        // arrange
        Movie movie = new Movie();
        List<Actor> actors = movie.getActors();
        Actor actor1 = new Actor();
        actor1.setName("Fred Brown");
        actors.add(actor1);
        Actor actor2 = new Actor();
        actor2.setName("George Smith");
        actors.add(actor2);

        // act & assert
        assertTrue(movie.isActor("Fred,George"));
        assertTrue(movie.isActor("george, fred"));
    }

    @Test
    void isActorFromBComoundNames3() {
        // arrange
        Movie movie = new Movie();
        List<Actor> actors = movie.getActors();
        Actor actor1 = new Actor();
        actor1.setName("  Fred   Brown  ");
        actors.add(actor1);

        // act & assert
        assertFalse(movie.isActor("George,fred"));
        assertFalse(movie.isActor(" fred , george "));
        assertFalse(movie.isActor("  george "));
        assertTrue(movie.isActor("  row "));
    }

}
