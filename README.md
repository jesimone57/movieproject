# Movie Project

Search Movies by Title
-------------
1. Search Movies
    - Find movies by partial title text.
    - Find movies in a given date range.
    - Optional sort (title, year, rating).
    - Optional filters: genre, release year.

Search Actor Filmographies by Actor
--------------


## How to run the single page Web UI
- Added a single‑page web UI to exercise every `GET` endpoint in `MovieController`.
- File created: `src/main/resources/static/index.html` (served automatically by Spring Boot at `/`).
- Light background, clean layout, and the page title is “Juliet Movie Project”.

### How to use
1. Run the Spring Boot app (default port 8080).
```
./gradlew bootRun
```
2. Open `http://localhost:8080/` in your browser.

## AI data generation prompts for the Movie Finder
* the same for 1959.  25 of the most popular, iconic, celebrated and award winning movies for that year.

* as a cinema lover I want the BEST movies for 1942.  25 of the most popular, iconic, celebrated and award winning movies for that year.

* as a cinema lover I want the BEST movies for 1930.  25 of the most popular, iconic, celebrated and award winning movies for that year.  Please provide a complete list of 25 films - no cheating.

* nice work.  now as a cinema lover I want the BEST movies for 1956.  30 of the most popular, iconic, celebrated and award winning movies for that year.  
Please provide a complete list with a minimum of 25 films.  more films is better.



look at the following JSON data structure describing a single movie:
```json

{
  "title": "All About Eve",
  "year": 1950,
  "director": {
    "name": "Joseph L. Mankiewicz",
    "imdb_person_url": "https://www.imdb.com/name/nm0542403/"
  },
  "production_studio": "20th Century Fox",
  "mpaa_rating": "Not Rated",
  "runtime_minutes": 138,
  "budget_usd_millions": 1.4,
  "gross_revenue_usd_millions": 8.4,
  "ratings": {
    "imdb": 8.2,
    "rotten_tomatoes_critic": "99%",
    "rotten_tomatoes_popcorn": "94%"
  },
  "genres": [
    "Drama"
  ],
  "actors": [
    {
      "name": "Bette Davis",
      "imdb_person_url": "https://www.imdb.com/name/nm0000012/"
    },
    {
      "name": "Anne Baxter",
      "imdb_person_url": "https://www.imdb.com/name/nm0000882/"
    },
    {
      "name": "George Sanders",
      "imdb_person_url": "https://www.imdb.com/name/nm0001695/"
    }
  ],
  "oscars_nominated": 14,
  "oscars_won": 6,
  "oscars_won_details": [
    "Best Picture",
    "Best Director",
    "Best Supporting Actor",
    "Best Writing, Screenplay",
    "Best Costume Design, Black-and-White",
    "Best Sound, Recording"
  ],
  "description": "A seemingly timid but secretly manipulative ingenue insinuates herself into the lives of an aging Broadway star and her circle of theater friends.",
  "afi_ranking": 16,
  "imdb_url": "https://www.imdb.com/title/tt0042192/",
  "rotten_tomatoes_url": "https://www.rottentomatoes.com/m/all_about_eve"
}
```

As a movie lover and as fan of cinema I would like a json dataset of a minimum of 50 unique elements
for the most popular, iconic, celebrated and award-winning movies for that year.
Use the above data structure as a sample of the type of data to return.  
Use movie ratings from IMDB and Rotten Tomatoes; find the film ranking,
if any, from the AFI website; list  the number of oscars nominated and won and which
oscars were won in an oscar_details array; runtime of the movie, director, actor with links
back to their imdb pages and all the other data you see above including budget and revenue numbers.  
Ensure the actors, director and movie IMDB links reference the correct IMDB page by matching its title.
Ensure there are no duplicate data elements!  Every movie in the dataset should be unique.
please provide a quality dataset for the most important movies for a given year without duplicates
or omitting important films.

If you need more than 50 films to do the list justice then do so.

Please include ALL movies from the given year with an IMDB rating of 7.5 or higher.
Please include ALL movies that were nominated for or won an oscar.
Please DO NOT allow duplicate movie titles in the resultant dataset

start with 1962.  The more movies the better.  Hope you can spare the CPU time!

how about 1994 please.  take your time and produce quality, complete and accurate data.  
you can add additional movies to ensure we have a  truly satisfying list. you can go to 40 or 50 movies if you wish.


# AI Data generation prompt for Actor Filmographies

As a movie lover and as fan of cinema I would like a json dataset with a minimum of 25 or more unique films for a given actor or actress.
Given the following example json dataset structure, produce a filmography for katherine hepburn which includes:
the following criteria:

* all her most notable and important films
* all her iconic and memorable roles
* all her most popular films and celebrated films
* all her films she won or was nominated for awards (oscar, bafta, golden globe, etc.)
* all films having an IMDB score of 7 or greater

```json
{
  "actor_profile": {
    "name": "Katharine Hepburn",
    "year_born": 1907,
    "year_died": 2003,
    "imdb_url": "https://www.imdb.com/name/nm0000031/",
    "image_url": "https://upload.wikimedia.org/wikipedia/commons/4/4a/Katharine_Hepburn_publicity_photograph.jpg",
    "year_of_first_film": 1932,
    "year_of_last_film": 1994,
    "number_of_feature_films_made": 44,
    "actor_awards": {
      "oscars_won": 4,
      "oscars_nominated": 12
    }
  },
  "filmography": [
    {
      "title": "Morning Glory",
      "year": 1933,
      "role": "Eva Lovelace",
      "role_description": "A naive, ambitious aspiring actress from a small town who is determined to become a great star on the Broadway stage, willing to sacrifice everything for her art.",
      "plot": "A naive, aspiring actress travels to New York to become a star.",
      "awards": "Oscar: Win – Best Actress",
      "ratings": {
        "imdb": 6.9,
        "rotten_tomatoes_critic": 88,
        "rotten_tomatoes_popcorn": 55
      },
      "imdb_url": "https://www.imdb.com/title/tt0024353/"
     },
    {
      "title": "Little Women",
      "year": 1933,
      "role": "Josephine \"Jo\" March",
      "role_description": "A tomboyish, independent-minded writer who resists societal expectations for women in the 19th century and is fiercely devoted to her sisters.",
      "plot": "The four March sisters grow up in New England during the Civil War.",
      "awards": "Venice Film Festival: Win – Best Actress",
      "ratings": {
        "imdb": 7.2,
        "rotten_tomatoes_critic": 89,
        "rotten_tomatoes_popcorn": 78
      },
      "imdb_url": "https://www.imdb.com/title/tt0024265/"
    }
  ]
}
```
Note: We used the following prompt for each actor thereafter:

please do the same for notable, important and award winning films for marlon brando

The resultant JSON files for actor filmeography may be found in the src/resourcee/actor-filmographies folder