# Golden Age of Hollywood Movie Project

## Motivation
As a huge cinema fan, I love old movies.  They hold a special interest for me.
Most of these movies have long been forgotten by today's movie goers.
Yet these films from the late 1920s to the mid 1960s are not to be missed. 
Movies that won academy awards for Best Picture of Best Actor or Actress provided great performances
and thrilled movie goers at the time.

And it's not just the movies that have been forgotten, it is the stars that have been forgotten as well.  One of my 
favorites is Bette Davis who delivered some powerful performances during her long career.  Not to be missed are movies like
"All About Eve" and "What Ever Happened to Baby Jane?". Another great film is James Cagney in "White Heat".
Suppose you enjoy movies directed by Alfred Hitchcock, you can list all his most important works and see which ones earned awards and 
budget and revenue numbers for each of his films.

## Overview
To create a golden age of hollywood movie explorer web application I relied upon the prodigious 
power of both ChatGPT and Google Gemini to research and produce the data including the following:
* one or more lists of noteworthy actors during this time-frame.  I started with a list of 100, then expanded to 125 and finally a list of 200 to draw from.
* for each early decade and later each year, I asked the AI to create the definitive list of great movies.  What defines "great"?  For great I used movies that won or were nominated for academy awards, iconic and legendary actors, etc.
* for each actor on the list, I asked AI to create their best movie filmographies - their best, award-winning roles and performances.
Obviously "great" for movies and actors may be subjectlively interpretted by different movie fans, so we have to contend ourselves with what the AI was able to "dig-up" or discover.
All this data is included in the main project folder either in the root folder of the src/main/resources folder.

## Limitations
As with any large undertaking unfortunately there are limitations that come into play.  These are:
1. I had to limit the number of actor/actresses to around 150 for the actor filmographies.  Some of your faviorites may be missing from the list.
All the important actors from this period are there.
2. I had to limit the number of movies per actor filmography to around 25. Some actors/actress had short careers.
Take Grace Kelley for example.  She only made 11 movies before leaving Hollywood.  Others had prolific careers spanning
decades with over a hundred acting credits.  I started with 10 movies per actor, then went to 20 and finally settled on a minimum of 25 if possible.
3. Unfortunately AI chatbot technology is not perfect and the data is not perfect.  There is gaps due to missing data that the chatbot could not find
as well as IMDB actor, movie or director links that are incorrect.  This is just a deficiency we must live with.
4. Approximately 4000 movies are currently in the movie database for searching.  This is not a comprehensive list, 
but is representative of the "best" movies from 1890 up through 2007.

Search Movies by Title
-------------
- Find movies by partial title text.
- Find movies in a given date range.
- Optional sort (title, year, rating).
- Optional filters: genres (1 or more), release year, actor

Search Actor Filmographies by Actor
--------------
- Find actors by partial actor name text.
- 


## How to use
1. Run the Spring Boot app (default port 8080).
```
./gradlew bootRun
```
2. Open `http://localhost:8080/` in your browser for movie search or
3. Open `http://localhost:8080/af.html` in your browser for actor filmography search


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

As a movie lover and as fan of cinema I would like a json dataset of a minimum of 53 unique elements
for the most popular, iconic, celebrated and award-winning movies for that year.  
Movies for inclusion should have a minimum IMDB rating of 6.5 and include all movies with 
IMDB rating of 7.5 or above.
Use the above data structure as a sample of the type of data to return.

* include 1 or more genres.
* Use movie ratings from IMDB and Rotten Tomatoes;
* find the film ranking from the AFI website if any;
* list  the number of oscars nominated and won and which oscars were won in an oscar_details array;
* runtime of the movie in minutes,
* director and actors with links back to their imdb pages
* budget for movie and gross revenue
* the studio which produced the movie
* all the other data you see above in the JSON

Ensure the actors, director and movie IMDB links reference the correct IMDB page by matching its title.
Ensure there are no duplicate data elements!  Every movie in the dataset should be unique.
please provide a quality dataset for the most important movies for a given year without duplicates
or omitting important films.    Start with the year 1950.

## AI Data generation prompt for Actor Filmographies

As a movie lover and as fan of cinema I would like a json dataset with a minimum of 25 or more unique films for a given actor or actress.
Given the following example json dataset structure, produce a filmography for katherine hepburn which includes:
the following criteria:

* all her most notable and important films
* all her iconic and memorable roles
* all her most popular films and celebrated films
* all her films she won or was nominated for awards (oscar, bafta, golden globe, etc.)
* all films having an IMDB score of 7 or greater
* American Film Institute (AFI) best of lists

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

The resultant JSON files for actor filmeography may be found in the src/main/resources/actor-filmographies folder