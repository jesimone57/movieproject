package com.example.demo.util;

import com.example.demo.model.Movie;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility to combine multiple decade JSON files into a single file,
 * removing duplicates by (title, year).
 */
public final class CombineMovies {

    private CombineMovies() { }

    public static List<Movie> readFromResource(String resource) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = CombineMovies.class.getClassLoader().getResourceAsStream(resource)) {
            if (is == null) {
                throw new IOException("Resource not found: " + resource);
            }
            return mapper.readValue(is, new TypeReference<List<Movie>>(){});
        }
    }

    public static List<Movie> combineUniqueByTitleYear(List<String> resources) throws IOException {
        Map<String, Movie> unique = new LinkedHashMap<>();
        for (String res : resources) {
            for (Movie m : readFromResource(res)) {
                String key = (m.getTitle() == null ? "" : m.getTitle().trim().toLowerCase()) + "|" + m.getYear();
                // keep first occurrence order-stable
                unique.putIfAbsent(key, m);
            }
        }
        return new ArrayList<>(unique.values());
    }

    public static void writePrettyJson(List<Movie> movies, Path outputPath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Files.createDirectories(outputPath.getParent());
        mapper.writerWithDefaultPrettyPrinter().writeValue(outputPath.toFile(), movies);
    }

    public static void main(String[] args) throws Exception {
        // Default inputs and output per the task
        List<String> inputs = List.of(
                "movies-decade-of-1930s.json",
                "movies-decade-of-1940s.json",
                "movies-decade-of-1950s.json"
        );
        Path output = Paths.get("src/main/resources/movies-of-the-1930s-1950s.json");

        List<Movie> combined = combineUniqueByTitleYear(inputs);
        writePrettyJson(combined, output);

        System.out.println("Combined movies written to: " + output.toAbsolutePath());
        System.out.println("Total unique movies: " + combined.size());
    }
}
