package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.model.ActorAwards;
import com.example.demo.model.ActorFilmography;
import com.example.demo.model.ActorProfile;
import com.example.demo.service.ActorFilmographyService;
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

@WebMvcTest(ActorFilmographyController.class)
class ActorFilmographyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ActorFilmographyService actorFilmographyService;


    @TestConfiguration
    static class TestConfig {
        @Bean
        public ActorFilmographyService actorFilmographyService() {
            return Mockito.mock(ActorFilmographyService.class);
        }
    }

    @Test
    void search_returnsEmptyArrayWhenNoResults() throws Exception {
        when(actorFilmographyService.filterActorFilmographies(
                any(), any(), any(), any(), any(), any()))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/filmographies/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void search_returnsActorList() throws Exception {
        ActorFilmography af = buildTestFilmography("Katharine Hepburn", 4, 12);
        when(actorFilmographyService.filterActorFilmographies(
                any(), any(), any(), any(), any(), any()))
                .thenReturn(List.of(af));

        mockMvc.perform(get("/api/filmographies/search").param("name", "Hepburn"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].actor_profile.name").value("Katharine Hepburn"));
    }

    @Test
    void search_withAllParams() throws Exception {
        when(actorFilmographyService.filterActorFilmographies(
                any(), any(), any(), any(), any(), any()))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/filmographies/search")
                        .param("name", "Hepburn")
                        .param("year", "1940")
                        .param("oscarsNominated", "12")
                        .param("oscarsWon", "4")
                        .param("filmListSearchText", "Morning Glory")
                        .param("sort", "year"))
                .andExpect(status().isOk());
    }

    @Test
    void duplicates_returnsMap() throws Exception {
        when(actorFilmographyService.findDuplicateActors())
                .thenReturn(Map.of("Cary Grant", 2));

        mockMvc.perform(get("/api/filmographies/duplicates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['Cary Grant']").value(2));
    }

    @Test
    void duplicatesExists_returnsFalse() throws Exception {
        when(actorFilmographyService.hasDuplicateActors()).thenReturn(false);

        mockMvc.perform(get("/api/filmographies/duplicates/exists"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }


    private ActorFilmography buildTestFilmography(String name, int oscarsWon, int oscarsNominated) {
        ActorAwards awards = new ActorAwards();
        awards.setOscarsWon(oscarsWon);
        awards.setOscarsNominated(oscarsNominated);

        ActorProfile profile = new ActorProfile();
        profile.setName(name);
        profile.setActorAwards(awards);

        ActorFilmography af = new ActorFilmography();
        af.setActorProfile(profile);
        af.setFilmography(List.of());
        return af;
    }
}

