# Movie Project

Business Need:
--------------
We need an **API MVP** that lets users quickly find and browse movies. A browser UI is **not required** for the MVP but a CLI tool would be a good start. Browser UI would be an amazing bonus.

Goal:
-----
Enable basic movie discovery via a clean, documented REST API.

MVP scope
-----

1. Search Movies
    - Find movies by partial title text.
    - Optional sort (title, year, rating).
    - Optional filters: genre, release year.
2. Numbered Results
    - For a given search, return a concise, numbered list of matching titles (id, title, year) so a user can pick one.
3. By Date Range
    - Accept a start_date and end_date and return movies released in that window.
4. Highest-Rated (Past Year)
    - Return the top N highest-rated movies released in the last 12 months.

Data expectations
-----------------
  - Use a seed or static dataset you control. External API integration is **not required** for the MVP.
  - Reasonable defaults are fine (e.g., rating scale, genres).
  - But you can use free External API integration if that makes more sense for your team to deliver the product quickly. For example, OMDB

Delivery expectations
----
  - **RESTful JSON API.**
  - **README.md** with API spec (endpoints, parameters, sample requests/responses, setup).
  - Tests (unit + integration) with all passing.
  - No compilation/build errors.
  - Endpoints meet the behaviors listed above.

Nice to have (optional)
----
  - Basic pagination on list endpoints.
  - Minimal web UI.

Out of scope (but potentially negotiable in discovery call)
----
  - Auth/user accounts, reviews/comments, caching, rate limiting, external paid APIs.

Customer Questions:
----
1. Does it matter about listing the movies out by alphabetical order or by specific time frames?
  - Up to us on the layout of how the information is displayed for the user, no preference in the order of the data listed
2. Do we want to force the customer to add wild card (* character for example) or do we want to add the wild card for them? 
  - The customer has no engineering prowess and doesn’t understand what this means. They just want it done on the backend, so they do not need to input anything.
3. Please clarify the expectations for this bullet - For a given search, return a concise, numbered list of matching titles (id, title, year) so a user can pick one. Top 10 results list that the customer can pick from. 
  - He doesn’t want to be overrun with options but do want options to go through. How we order it again, that is up to the team. 
  - We could think about filtering in iteration for V1 or V2.

User Stories - Movie Discovery API MVP
----

### Story 1: Search Movies by Title
**As a** movie enthusiast \
**I want** to search for movies by partial title text \
**So that** I can quickly find movies without knowing the exact title

### Acceptance Criteria:

#### AC1: Scenario: Basic title search

**Given** the movie database contains movies with various titles \
**When** I send a GET request to "/api/movies/search?q=dark" \
**Then** I should receive a 200 status code \
**And** the response should contain movies with "dark" in the title \
**And** each movie should include id, title, and year fields

#### AC2: Scenario: Search with sort by title

**Given** the movie database contains movies matching my search \
**When** I send a GET request to "/api/movies/search?q=dark&sort=title" \
**Then** the results should be ordered alphabetically by title

#### AC3: Scenario: Search with sort by year

**Given** the movie database contains movies matching my search \
**When** I send a GET request to "/api/movies/search?q=dark&sort=year" \
**Then** the results should be ordered by release year

#### AC4: Scenario: Search with sort by rating

**Given** the movie database contains movies matching my search \
**When** I send a GET request to "/api/movies/search?q=dark&sort=rating" \
**Then** the results should be ordered by rating (highest to lowest)

#### AC5: Scenario: Search with genre filter

**Given** the movie database contains movies of various genres \
**When** I send a GET request to "/api/movies/search?q=dark&genre=thriller" \
**Then** all results should be in the "thriller" genre \
**And** all results should contain "dark" in the title

#### AC6: Scenario: Search with release year filter

**Given** the movie database contains movies from various years \
**When** I send a GET request to "/api/movies/search?q=knight&year=2008" \
**Then** all results should be released in 2008 \
**And** all results should contain "knight" in the title

#### AC7: Scenario: Search with combined filters

**Given** the movie database contains movies with various attributes \
**When** I send a GET request to "/api/movies/search?q=dark&genre=action&year=2012&sort=rating" \
**Then** results should contain "dark" in the title \
**And** results should be in the "action" genre \
**And** results should be released in 2012 \
**And** results should be sorted by rating

#### AC8: Scenario: No results found

**Given** the movie database is populated \
**When** I send a GET request to "/api/movies/search?q=xyznonexistent" \
**Then** I should receive a 200 status code \
**And** the response should contain an empty results array

### Points: 3

Story 2: View Numbered Search Results
-----

**As a** user \
**I want** to see a numbered list of search results with concise information \
**So that** I can easily identify and select a specific movie

### Acceptance Criteria:

#### AC1: Scenario: Search results include sequential numbering

**Given** the movie database contains 10 movies matching "matrix" \
**When** I send a GET request to "/api/movies/search?q=matrix" \
**Then** each result should include a sequential number starting from 1 \
**And** each result should include id, title, and year \
**And** the results should be in a concise format

#### AC2: Scenario: Numbered results maintain order with sorting

Given the movie database contains movies matching my search \
When I send a GET request to "/api/movies/search?q=star&sort=year" \
Then the numbering should start at 1 \
And the numbering should increment sequentially \
And the order should match the year sort order

#### AC3: Scenario: Paginated results show correct numbering

Given the API supports pagination \
When I send a GET request to "/api/movies/search?q=love&page=2&limit=10" \
Then the first result should be numbered 11 \
And subsequent results should increment from 11

### Points: 1

Story 3: Find Movies by Date Range
------
As a user \
I want to find movies released within a specific date range \
So that I can discover movies from a particular time period

### Acceptance Criteria:

#### AC1: Scenario: Retrieve movies within a valid date range

Given the movie database contains movies from various years \
When I send a GET request to "/api/movies?start_date=2020-01-01&end_date=2020-12-31" \
Then I should receive a 200 status code \
And all movies should have release dates between 2020-01-01 and 2020-12-31 (inclusive) \
And each movie should include id, title, and year

#### AC2: Scenario: Date range with no results

Given the movie database has no movies from 1800 \
When I send a GET request to "/api/movies?start_date=1800-01-01&end_date=1800-12-31" \
Then I should receive a 200 status code \
And the response should contain an empty results array

#### AC3: Scenario: Invalid date range (end before start)

Given the API validates date ranges \
When I send a GET request to "/api/movies?start_date=2020-12-31&end_date=2020-01-01" \
Then I should receive a 400 status code \
And the response should include an error message about invalid date range

#### AC4: Scenario: Missing required date parameters

Given the endpoint requires both start_date and end_date \
When I send a GET request to "/api/movies?start_date=2020-01-01" \
Then I should receive a 400 status code \
And the response should indicate the missing parameter

#### AC5: Scenario: Date range with sort option

Given the movie database contains movies in the date range \
When I send a GET request to "/api/movies?start_date=2020-01-01&end_date=2021-12-31&sort=rating" \
Then all movies should be within the date range \
And the results should be sorted by rating

### Points: 2

Story 4: View Highest-Rated Movies from Past Year
-------------------------------------------------

As a user \
I want to see the top N highest-rated movies from the past 12 months \
So that I can discover the best recent releases

### Acceptance Criteria:

#### AC1: Scenario: Get top 10 highest-rated movies from past year

Given today's date is 2024-03-15 \
And the movie database contains movies with ratings \
When I send a GET request to "/api/movies/top-rated?limit=10" \
Then I should receive a 200 status code \
And the response should contain exactly 10 movies \
And all movies should be released between 2023-03-15 and 2024-03-15 \
And the movies should be ordered by rating (highest first) \
And each movie should include id, title, year, and rating

#### AC2: Scenario: Get top 5 highest-rated movies from past year

Given the movie database contains rated movies from the past year \
When I send a GET request to "/api/movies/top-rated?limit=5" \
Then the response should contain exactly 5 movies \
And they should be the 5 highest-rated from the past 12 months

#### AC3: Scenario: Default limit when not specified

Given the API has a default limit for top-rated movies \
When I send a GET request to "/api/movies/top-rated" \
Then I should receive a default number of results (e.g., 10) \
And all should be from the past 12 months \
And sorted by rating descending

#### AC4: Scenario: Fewer movies than requested limit

Given only 3 movies exist in the past 12 months \
When I send a GET request to "/api/movies/top-rated?limit=10" \
Then I should receive exactly 3 movies \
And they should be sorted by rating

#### AC5: Scenario: Invalid limit parameter

Given the API validates the limit parameter \
When I send a GET request to "/api/movies/top-rated?limit=-5" \
Then I should receive a 400 status code \
And the response should include an error message

### Points: 2


Story 5: CLI Tool for API Interaction (Stretch)
-----------------------------------------------

As a developer or power user \
I want a command-line interface to interact with the movie API \
So that I can quickly test and use movie discovery features without a browser

### Acceptance Criteria:

#### AC1: Scenario: Search movies via CLI

Given the CLI tool is installed and configured \
When I run "movie-cli search 'dark knight'" \
Then I should see a numbered list of matching movies \
And each entry should show the movie id, title, and year

#### AC2: Scenario: Search with sort option via CLI

Given the CLI tool supports sorting \
When I run "movie-cli search 'matrix' --sort rating" \
Then the results should be sorted by rating \
And displayed in a readable format

#### AC3: Scenario: Search with filters via CLI

Given the CLI tool supports filters \
When I run "movie-cli search 'love' --genre romance --year 2020" \
Then the results should be filtered by genre and year \
And displayed with numbering

#### AC4: Scenario: Get movies by date range via CLI

Given the CLI tool supports date range queries \
When I run "movie-cli date-range --start 2020-01-01 --end 2020-12-31" \
Then I should see all movies released in 2020 \
And they should be numbered

#### AC5: Scenario: Get top-rated movies via CLI

Given the CLI tool supports top-rated queries
When I run "movie-cli top-rated --limit 5"
Then I should see the top 5 rated movies from the past year
And they should be numbered and sorted by rating

#### AC6: Scenario: CLI help documentation

Given the CLI tool is installed \
When I run "movie-cli --help" \
Then I should see usage documentation \
And all available commands and options should be listed

#### AC7: Scenario: CLI error handling

Given the API is unreachable \
When I run any CLI command \
Then I should see a user-friendly error message \
And the CLI should exit with a non-zero status code

### Points: 3


Story 6: Web UI (Stretch)
-------

As a non-technical user \
I want a web interface to browse movies \
So that I can use the service without command-line tools \

### Acceptance Criteria:

#### AC1: Scenario: Search movies via web interface

Given I am on the movie discovery web page \
When I enter "inception" in the search box \
And I click the search button \
Then I should see a numbered list of matching movies \
And each movie should display its title and year

#### AC2: Scenario: Apply filters in web UI

Given I am viewing search results \
When I select "Action" from the genre dropdown \
And I select "Sort by Rating" from the sort dropdown \
Then the results should update to show only action movies \
And they should be sorted by rating

#### AC3: Scenario: View top-rated movies in web UI

Given I am on the movie discovery web page \
When I click on "Top Rated This Year" \
Then I should see the highest-rated movies from the past 12 months \
And they should be displayed in a numbered list

#### AC4: Scenario: Browse movies by date range in web UI

Given I am on the movie discovery web page \
When I select a start date of "2020-01-01" \
And I select an end date of "2020-12-31" \
And I click "Find Movies" \
Then I should see all movies released in 2020 \
And they should be numbered

### Point:  3

## How to run CLI

java -jar build/lib/demo-0.0.1-SNAPSHOT.jar search "dark knight"

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

## AI data generation prompts
* the same for 1959.  25 of the most popular, iconic, celebrated and award winning movies for that year.

* as a cinema lover I want the BEST movies for 1942.  25 of the most popular, iconic, celebrated and award winning movies for that year.

* as a cinema lover I want the BEST movies for 1930.  25 of the most popular, iconic, celebrated and award winning movies for that year.  Please provide a complete list of 25 films - no cheating.

* nice work.  now as a cinema lover I want the BEST movies for 1956.  30 of the most popular, iconic, celebrated and award winning movies for that year.  
Please provide a complete list with a minimum of 25 films.  more films is better.



look at the following json element describing a movie:
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
As a movie lover and as fan of cinema i would like a json dataset of a minimum of 25 unique elements
for the most popular, iconic, celebrated and award winning movies for that year. 
Use the above data structure as a sample of the type of data to return.  
Use movie ratings from IMDB and Rotten Tomatoes; find the film ranking, 
if any, from the AFI website; list  the number of oscars nominated and won and which 
oscars were won in a oscar_details array; runtime of the movie, director, actor with links 
back to their imdb pages and all the other data you see above including budget and revenue numbers.  
please provide a quality dataset for the most important movies for a given year without duplicates 
or omitting important films.   
If you need more that 25 films the list the most celebrated and iconic movies then do so.

start with 1957.  The more movies the better.  Hope you can spare the CPU time!

how about 1993 please.  take your time and produce quality, complete and accurate data.  
you can add additional movies to ensure we have a  truly satisfying list. you can go to 40 or 50 movies if you wish.
```