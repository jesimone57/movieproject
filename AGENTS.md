# AGENTS.md — Golden Age of Hollywood Movie Project

## Project Overview
Spring Boot 3.x web application serving a "Golden Age of Hollywood" movie explorer. All data is **static JSON on the classpath** — no database. The app exposes REST APIs consumed by vanilla-JS frontend pages.

- **Java 21**, Gradle wrapper, Lombok, Apache Commons Lang 3, Spring Shell
- Packaged as a WAR (`./gradlew bootWar`); runs embedded Tomcat via `./gradlew bootRun`
- Two UIs: `http://localhost:8080/` (movie search) and `http://localhost:8080/af.html` (actor filmographies)

## Key Developer Commands
```bash
./gradlew bootRun          # Start the app on port 8080
./gradlew test             # Run all JUnit 5 tests
./gradlew bootWar          # Build deployable WAR
./gradlew combineMovies    # Utility: merge 1930s–1950s decade JSON files into movies-of-the-1930s-1950s.json
```

## Architecture & Data Flow

```
src/main/resources/
  movies-by-year/movies-YYYY.json       # ~4,200 movies total, one file per year (1890s–2010)
  actor-filmographies/ACTOR-NAME.json   # ~250 actors, one file each (kebab-case)
  static/index.html, af.html            # Frontend pages
```

**At startup**, `MovieService` and `ActorFilmographyService` scan their classpath folders with `PathMatchingResourcePatternResolver` and load all `*.json` files into in-memory `List<Movie>` / `List<ActorFilmography>`. Each loader tries to parse as a single object first, then falls back to a JSON array — malformed files are silently skipped.

**Filtering** happens entirely in-memory via Java Streams in `filterMovies()` and `filterActorFilmographies()`. All string comparisons use `StringUtils.containsIgnoreCase` (case-insensitive partial match). Multi-value genre/actor filtering uses comma-delimited tokens (e.g., `genre=Rom,Com`).

## REST API Endpoints

| Endpoint | Description |
|---|---|
| `GET /api/movies/search?title=&genre=&director=&actor=&oscarWon=&studio=&minRating=&yearStart=&yearEnd=&sort=` | Main movie search |
| `GET /api/movies/duplicates` | Map of duplicate movie titles → count |
| `GET /api/filmographies/search?name=&year=&oscarsWon=&oscarsNominated=&filmListSearchText=&sort=` | Actor filmography search |

Valid `sort` values: `title` (default), `year`, `rating`.

## Data Model Conventions
- `Movie` fields use `@JsonProperty` to map snake_case JSON → camelCase Java (e.g., `production_studio` → `studio`, `oscars_won_details` → `oscarsWonDetails`).
- `ActorFilmography` wraps `ActorProfile` + `List<ActorMovie>` — matches the `actor_profile` / `filmography` JSON keys.
- All models use Lombok `@Data` (no hand-written getters/setters).
- `Movie.num` is a transient sequence number assigned after filtering/sorting by `numberMovies()` — it is not stored in JSON.

## Adding New Data
- **New movie year**: add `movies-YYYY.json` (array of Movie objects) to `src/main/resources/movies-by-year/`. It is auto-loaded at startup.
- **New actor filmography**: add `firstname-lastname.json` (single `ActorFilmography` object) to `src/main/resources/actor-filmographies/`. Filename must be kebab-case. See `katharine-hepburn.json` as a reference.
- JSON schema for both types is documented with examples in `README.md`.

## Testing Patterns
- Tests instantiate services directly with a specific resource file: `new MovieService("movies-decade-of-1930s.json")` — no Spring context needed.
- `MovieProjectUnitTest` is the main test suite for `MovieService`; `ActorFilmographyServiceTest` covers `ActorFilmographyService`.
- Model-level tests live in `src/test/java/com/example/demo/model/`.
- Test data uses real classpath resources (e.g., `movies-by-year/movies-1930.json`, `movies-decade-of-1930s.json`).

## Utility Classes
- `DuplicateTitleChecker` (`main` method) — scans all year files for duplicate titles; run directly from IDE.
- `CombineMovies` (`main` method, also `./gradlew combineMovies`) — merges decade JSON files, deduplicates by `(title, year)`.
- `FilmographyCounter` — utility for counting filmography entries.
- `MovieCommands` — Spring Shell component; `search` command (stub, not wired to service yet).

## Key Files
| File | Purpose |
|---|---|
| `src/main/java/com/example/demo/service/MovieService.java` | Core movie loading & filtering logic |
| `src/main/java/com/example/demo/service/ActorFilmographyService.java` | Core actor loading & filtering logic |
| `src/main/java/com/example/demo/model/Movie.java` | Movie domain model + filter helper methods |
| `src/main/resources/movies-by-year/` | All per-year movie JSON files |
| `src/main/resources/actor-filmographies/` | All per-actor filmography JSON files |
| `src/main/resources/static/index.html` | Movie search frontend |
| `src/main/resources/static/af.html` | Actor filmography frontend |
| `README.md` | Full JSON schema examples and AI data generation prompts |

