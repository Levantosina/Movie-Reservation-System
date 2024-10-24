DROP TABLE IF EXISTS movies;
CREATE TABLE movies (
                        movie_id SERIAL PRIMARY KEY,
                        movie_name TEXT NOT NULL UNIQUE,
                        year INTEGER NOT NULL,
                        country TEXT NOT NULL,
                        genre TEXT NOT NULL,
                        description TEXT NOT NULL
);

INSERT INTO movies(movie_name, year, country, genre, description) VALUES ('Zorojuro',2023,'JP','Action','Like a dragon');