package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.model.Director;
import com.example.demo.model.Movie;
import com.example.demo.model.MovieFilter;
import com.example.demo.model.Ratings;
import com.example.demo.service.MovieService;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MovieController.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieService movieService;


    @TestConfiguration
    static class TestConfig {
        @Bean
        public MovieService movieService() {
            return Mockito.mock(MovieService.class);
        }
    }

    @Test
    void search_returnsEmptyArrayWhenNoResults() throws Exception {
        when(movieService.filterMovies(any(MovieFilter.class))).thenReturn(List.of());

        mockMvc.perform(get("/api/movies/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void search_returnsMovieList() throws Exception {
        Movie movie = buildTestMovie("All About Eve", 1950, 8.2);
        when(movieService.filterMovies(any(MovieFilter.class))).thenReturn(List.of(movie));

        mockMvc.perform(get("/api/movies/search").param("title", "Eve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("All About Eve"))
                .andExpect(jsonPath("$[0].year").value(1950));
    }

    @Test
    void search_numFieldNotInResponse() throws Exception {
        Movie movie = buildTestMovie("Casablanca", 1942, 8.5);
        movie.setNum(42);
        when(movieService.filterMovies(any(MovieFilter.class))).thenReturn(List.of(movie));

        mockMvc.perform(get("/api/movies/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].num").doesNotExist());
    }

    @Test
    void search_invalidSortReturns400() throws Exception {
        when(movieService.filterMovies(any(MovieFilter.class)))
                .thenThrow(new IllegalArgumentException("sort must be either title or year or rating"));

        mockMvc.perform(get("/api/movies/search").param("sort", "invalid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("sort must be either title or year or rating"));
    }

    @Test
    void search_yearOutOfRangeReturns400() throws Exception {
        when(movieService.filterMovies(any(MovieFilter.class)))
                .thenThrow(new IllegalArgumentException("year start must be between 1878 and 2031"));

        mockMvc.perform(get("/api/movies/search").param("yearStart", "1800"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void search_allParamsPassedToService() throws Exception {
        when(movieService.filterMovies(any(MovieFilter.class))).thenReturn(List.of());

        mockMvc.perform(get("/api/movies/search")
                        .param("title", "Gone")
                        .param("genre", "Drama")
                        .param("director", "Fleming")
                        .param("actor", "Gable")
                        .param("oscarWon", "Best Picture")
                        .param("studio", "MGM")
                        .param("minRating", "7.5")
                        .param("yearStart", "1939")
                        .param("yearEnd", "1939")
                        .param("sort", "rating"))
                .andExpect(status().isOk());
    }

    @Test
    void duplicates_returnsMap() throws Exception {
        when(movieService.findDuplicateTitles()).thenReturn(Map.of("Scarface", 2));

        mockMvc.perform(get("/api/movies/duplicates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Scarface").value(2));
    }

    @Test
    void duplicates_returnsEmptyMapWhenNone() throws Exception {
        when(movieService.findDuplicateTitles()).thenReturn(Map.of());

        mockMvc.perform(get("/api/movies/duplicates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void duplicatesExists_returnsTrue() throws Exception {
        when(movieService.hasDuplicateTitles()).thenReturn(true);

        mockMvc.perform(get("/api/movies/duplicates/exists"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void duplicatesExists_returnsFalse() throws Exception {
        when(movieService.hasDuplicateTitles()).thenReturn(false);

        mockMvc.perform(get("/api/movies/duplicates/exists"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }


    private Movie buildTestMovie(String title, int year, double imdbRating) {
        Movie m = new Movie();
        m.setTitle(title);
        m.setYear(year);
        Ratings ratings = new Ratings();
        ratings.setImdb(imdbRating);
        m.setRatings(ratings);
        Director director = new Director();
        director.setName("Test Director");
        m.setDirector(director);
        return m;
    }
}

