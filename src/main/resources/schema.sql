DROP TABLE IF EXISTS films, films_genres, users, friendship, likes, directors, films_directors, events, reviews,
    review_likes CASCADE;

CREATE TABLE IF NOT EXISTS genres
(
    id 		INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name 	VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS rating
(
    id 		INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name 	VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS films
(
    id           INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name         VARCHAR(200) NOT NULL,
    description  VARCHAR(200),
    release_date DATE,
    duration     INTEGER,
    mpa          INTEGER REFERENCES rating (id) ON DELETE CASCADE,
    CONSTRAINT name_not_blank check (name <> ''),
    CONSTRAINT duration_is_positive check (duration is null or duration > 0)
);

CREATE TABLE IF NOT EXISTS films_genres
(
    id 		INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id     INTEGER REFERENCES films (id) ON DELETE CASCADE,
    genre_id    INTEGER REFERENCES genres (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS directors
(
    id 		INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name    VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS films_directors
(
    id 		INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id     INTEGER REFERENCES films (id) ON DELETE CASCADE,
    director_id INTEGER REFERENCES directors (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users
(
    id 		INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email    	VARCHAR,
    login    	VARCHAR,
    name 	VARCHAR,
    birthday 	DATE,
    CONSTRAINT email_not_blank check (email <> ' '),
    CONSTRAINT login_not_blank check (login <> ' '),
    CONSTRAINT birthday_in_past check (birthday is null or birthday < CURRENT_DATE)
);

CREATE TABLE IF NOT EXISTS friendship
(
    id 		INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id	INTEGER REFERENCES users (id) ON DELETE CASCADE,
    friend_id   INTEGER REFERENCES users (id) ON DELETE CASCADE,
    is_confirmed  BOOLEAN DEFAULT false
);

CREATE TABLE IF NOT EXISTS likes
(
    id 		INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id  	INTEGER REFERENCES films (id) ON DELETE CASCADE,
    user_id  	INTEGER REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reviews
(
    id          INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    content     VARCHAR,
    is_positive  BOOLEAN,
    user_id  	INTEGER REFERENCES users (id),
    film_id  	INTEGER REFERENCES films (id)
);

CREATE TABLE IF NOT EXISTS review_likes
(
    review_id  	INTEGER REFERENCES reviews (id) ON DELETE CASCADE,
    user_id  	INTEGER REFERENCES users (id) ON DELETE CASCADE,
    is_like     BOOLEAN,
        PRIMARY KEY (review_id,  user_id )
);

CREATE TABLE IF NOT EXISTS events
(
    event_id          INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    timestamp   LONG,
    event_type  VARCHAR(10) NOT NULL,
    operation   VARCHAR(10) NOT NULL,
    user_id  	INTEGER REFERENCES users (id) ON DELETE CASCADE,
    entity_id   INTEGER
);


