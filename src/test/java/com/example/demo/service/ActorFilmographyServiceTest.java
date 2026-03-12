package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.demo.model.ActorFilmography;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActorFilmographyServiceTest {

    private static final String TEST_RESOURCE = "actor-movies-sample.json";
    private ActorFilmographyService actorFilmographyService;

    @BeforeEach
    void setUp() {
        actorFilmographyService = new ActorFilmographyService(TEST_RESOURCE);
    }

    @Test
    void getFilmographies() {
        List<ActorFilmography> filmographies = actorFilmographyService.filterActorFilmographies(null,
                null, null, null, null);
        assertNotNull(filmographies);
        assertFalse(filmographies.isEmpty());
    }

    @Test
    void getActorByName() {
        String name = "streep";
        List<ActorFilmography> filmographies = actorFilmographyService.filterActorFilmographies(name,
                null, null, null, null);
        assertNotNull(filmographies);
        assertFalse(filmographies.isEmpty());
        assertEquals(1, filmographies.size());
        assertEquals("Meryl Streep", filmographies.getFirst().getActorProfile().getName());
    }

    @Test
    void getActorByPartialName() {
        String name = "e";
        List<ActorFilmography> filmographies = actorFilmographyService.filterActorFilmographies(name,
                null, null, null, null);
        assertNotNull(filmographies);
        assertFalse(filmographies.isEmpty());
        assertEquals(2, filmographies.size());
        assertEquals("Katharine Hepburn", filmographies.getFirst().getActorProfile().getName());
        assertEquals("Meryl Streep", filmographies.getLast().getActorProfile().getName());
    }

    @Test
    void getActorByPartialNameWhitespace() {
        String name = " rlo  ";
        List<ActorFilmography> filmographies = actorFilmographyService.filterActorFilmographies(name,
                null, null, null, null);
        assertNotNull(filmographies);
        assertFalse(filmographies.isEmpty());
        assertEquals(1, filmographies.size());
        assertEquals("Marlon Brando", filmographies.getFirst().getActorProfile().getName());
    }

    @Test
    void getActorByYear() {
        Integer year = 1932;
        List<ActorFilmography> filmographies = actorFilmographyService.filterActorFilmographies(null,
                year, null, null, null);
        assertNotNull(filmographies);
        assertFalse(filmographies.isEmpty());
        assertEquals(2, filmographies.size());
        for (ActorFilmography af : filmographies) {
            assertTrue(af.getActorProfile().isInRange(year));
        }
    }

    @Test
    void getActorsByOscarsWon() {
        Integer oscarsWon = 4;
        List<ActorFilmography> filmographies = actorFilmographyService.filterActorFilmographies(null,
                null, null, oscarsWon, null);
        assertNotNull(filmographies);
        assertFalse(filmographies.isEmpty());
        for (ActorFilmography af : filmographies) {
            assertEquals(oscarsWon, af.getActorProfile().getActorAwards().getOscarsWon());
        }
    }

    @Test
    void getActorsByOscarsNominated() {
        Integer oscarsNominated = 12;
        List<ActorFilmography> filmographies = actorFilmographyService.filterActorFilmographies(null,
                null, oscarsNominated, null, null);
        assertNotNull(filmographies);
        assertFalse(filmographies.isEmpty());
        for (ActorFilmography af : filmographies) {
            assertEquals(oscarsNominated, af.getActorProfile().getActorAwards().getOscarsNominated());
        }
    }

    @Test
    void getActorsByFilmographySearchText() {
        String searchText = "Morning Glory";
        List<ActorFilmography> filmographies = actorFilmographyService.filterActorFilmographies(null,
                null, null, null, searchText);
        assertNotNull(filmographies);
        assertFalse(filmographies.isEmpty());
        for (ActorFilmography af : filmographies) {
            assertTrue(af.isTextInCurrentFilmographyDisplayText(searchText));
        }
    }

    @Test
    void getActorByNameAndYear() {
        String name = "Hepburn";
        Integer year = 1940;
        List<ActorFilmography> filmographies = actorFilmographyService.filterActorFilmographies(name,
                year, null, null, null);
        assertNotNull(filmographies);
        assertFalse(filmographies.isEmpty());
        for (ActorFilmography af : filmographies) {
            assertTrue(af.isActorName(name));
            assertTrue(af.getActorProfile().isInRange(year));
        }
    }

    @Test
    void findDuplicateActors_noDuplicatesInSampleFile() {
        assertFalse(actorFilmographyService.hasDuplicateActors());
        assertTrue(actorFilmographyService.findDuplicateActors().isEmpty());
    }

    @Test
    void sortFilmographiesByTitle() {
        List<ActorFilmography> filmographies = actorFilmographyService.filterActorFilmographies(
                "Hepburn", null, null, null, null, "title");
        assertNotNull(filmographies);
        assertFalse(filmographies.isEmpty());
        List<com.example.demo.model.ActorMovie> films = filmographies.getFirst().getFilmography();
        for (int i = 0; i < films.size() - 1; i++) {
            assertTrue(films.get(i).getTitle().compareToIgnoreCase(films.get(i + 1).getTitle()) <= 0);
        }
    }

    @Test
    void sortFilmographiesByYear() {
        List<ActorFilmography> filmographies = actorFilmographyService.filterActorFilmographies(
                "Hepburn", null, null, null, null, "year");
        assertNotNull(filmographies);
        assertFalse(filmographies.isEmpty());
        List<com.example.demo.model.ActorMovie> films = filmographies.getFirst().getFilmography();
        for (int i = 0; i < films.size() - 1; i++) {
            Integer y1 = films.get(i).getYear();
            Integer y2 = films.get(i + 1).getYear();
            if (y1 != null && y2 != null) {
                assertTrue(y1 <= y2);
            }
        }
    }

    @Test
    void sortFilmographiesByRating() {
        List<ActorFilmography> filmographies = actorFilmographyService.filterActorFilmographies(
                "Hepburn", null, null, null, null, "rating");
        assertNotNull(filmographies);
        assertFalse(filmographies.isEmpty());
        List<com.example.demo.model.ActorMovie> films = filmographies.getFirst().getFilmography();
        for (int i = 0; i < films.size() - 1; i++) {
            double r1 = films.get(i).getRatings() != null ? films.get(i).getRatings().getImdb() : Double.MIN_VALUE;
            double r2 = films.get(i + 1).getRatings() != null ? films.get(i + 1).getRatings().getImdb() : Double.MIN_VALUE;
            assertTrue(r1 >= r2, "Ratings should be descending");
        }
    }
}
