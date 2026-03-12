package com.example.demo.exception;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.controller.MovieController;
import com.example.demo.model.MovieFilter;
import com.example.demo.service.MovieService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests that GlobalExceptionHandler translates service exceptions
 * into structured JSON error responses instead of raw 500 stack traces.
 */
@WebMvcTest(MovieController.class)
class GlobalExceptionHandlerTest {

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
    void illegalArgument_invalidSort_returns400WithErrorMessage() throws Exception {
        when(movieService.filterMovies(any(MovieFilter.class)))
                .thenThrow(new IllegalArgumentException("sort must be either title or year or rating"));

        mockMvc.perform(get("/api/movies/search").param("sort", "bogus"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("sort must be either title or year or rating"));
    }

    @Test
    void illegalArgument_yearOutOfRange_returns400() throws Exception {
        when(movieService.filterMovies(any(MovieFilter.class)))
                .thenThrow(new IllegalArgumentException("year start must be between 1878 and 2031"));

        mockMvc.perform(get("/api/movies/search").param("yearStart", "1800"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void illegalArgument_yearRangeInverted_returns400() throws Exception {
        when(movieService.filterMovies(any(MovieFilter.class)))
                .thenThrow(new IllegalArgumentException("year start 1950 must be before year end 1940"));

        mockMvc.perform(get("/api/movies/search").param("yearStart", "1950").param("yearEnd", "1940"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void unexpectedException_returns500WithGenericMessage() throws Exception {
        when(movieService.filterMovies(any(MovieFilter.class)))
                .thenThrow(new RuntimeException("internal database error"));

        mockMvc.perform(get("/api/movies/search"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("An unexpected error occurred. Please try again."));
    }

    @Test
    void unexpectedException_doesNotLeakSensitiveInfo() throws Exception {
        when(movieService.filterMovies(any(MovieFilter.class)))
                .thenThrow(new RuntimeException("sensitive database password info"));

        mockMvc.perform(get("/api/movies/search"))
                .andExpect(status().isInternalServerError())
                // Error response must not contain the raw exception message
                .andExpect(jsonPath("$.error").value("An unexpected error occurred. Please try again."));
    }
}

