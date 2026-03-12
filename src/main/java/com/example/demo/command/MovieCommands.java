package com.example.demo.command;

import com.example.demo.model.Movie;
import com.example.demo.service.MovieService;
import java.util.List;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class MovieCommands {

    private final MovieService movieService;

    public MovieCommands(MovieService movieService) {
        this.movieService = movieService;
    }

    @ShellMethod(key = "search", value = "Search movies by title (partial match, case-insensitive)")
    public String search(
            @ShellOption(defaultValue = "") String title,
            @ShellOption(defaultValue = "title") String sort) {

        if (title.isBlank()) {
            return "Usage: search --title <text> [--sort title|year|rating]";
        }

        List<Movie> results = movieService.getMovieByPartialTitle(title.trim(), sort);
        if (results.isEmpty()) {
            return "No movies found matching: \"" + title + "\"";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Found ").append(results.size()).append(" movie(s) matching \"").append(title).append("\":\n");
        results.forEach(m -> sb
                .append(String.format("  %4d  %-50s  IMDb: %.1f%n",
                        m.getYear(), m.getTitle(), m.getImdbRating())));
        return sb.toString();
    }
}
