package com.example.demo.model;

/**
 * Immutable value object encapsulating all movie filter and sort parameters.
 * Replaces the 10-parameter method signature on MovieService.filterMovies().
 * Use {@link #builder()} to construct instances conveniently.
 */
@SuppressWarnings("squid:S107")  // Ignore unused record warning - used by MovieService
public record MovieFilter(
        String title,
        String genre,
        Double minRating,
        Integer yearStart,
        Integer yearEnd,
        String sort,
        String director,
        String actor,
        String oscarWon,
        String studio) {

    /** Default sort applied when none is specified. */
    public static final String DEFAULT_SORT = "title";

    /** Returns a new {@link Builder} with {@code sort} pre-set to {@value DEFAULT_SORT}. */
    @SuppressWarnings("squid:S2325")  // Ignore unused builder method warning - fluent API uses all
    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings({"squid:S1068", "squid:S1144"})  // Suppress unused field/method warnings for builder pattern
    public static final class Builder {
        private String title;
        private String genre;
        private String director;
        private String actor;
        private String oscarWon;
        private String studio;
        private String sort = DEFAULT_SORT;
        private Double minRating;
        private Integer yearStart;
        private Integer yearEnd;

        public Builder title(String v)      { title = v;      return this; }
        public Builder genre(String v)      { genre = v;      return this; }
        public Builder minRating(Double v)  { minRating = v;  return this; }
        public Builder yearStart(Integer v) { yearStart = v;  return this; }
        public Builder yearEnd(Integer v)   { yearEnd = v;    return this; }
        public Builder sort(String v)       { sort = v;       return this; }
        public Builder director(String v)   { director = v;   return this; }
        public Builder actor(String v)      { actor = v;      return this; }
        public Builder oscarWon(String v)   { oscarWon = v;   return this; }
        public Builder studio(String v)     { studio = v;     return this; }

        public MovieFilter build() {
            return new MovieFilter(title, genre, minRating, yearStart, yearEnd,
                    sort, director, actor, oscarWon, studio);
        }
    }
}

