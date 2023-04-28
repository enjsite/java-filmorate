DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS rating CASCADE;
DROP TABLE IF EXISTS films CASCADE;
DROP TABLE IF EXISTS films_genres CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS friendship CASCADE;
DROP TABLE IF EXISTS likes CASCADE;
DROP TABLE IF EXISTS directors CASCADE;
DROP TABLE IF EXISTS films_directors CASCADE;

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
    mpa          INTEGER REFERENCES rating (id),
    CONSTRAINT name_not_blank check (name <> ''),
    CONSTRAINT duration_is_positive check (duration is null or duration > 0)
);

CREATE TABLE IF NOT EXISTS films_genres
(
    id 		INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id     INTEGER REFERENCES films (id),
    genre_id    INTEGER REFERENCES genres (id)
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
    user_id	INTEGER REFERENCES users (id),
    friend_id   INTEGER REFERENCES users (id),
    is_confirmed  BOOLEAN DEFAULT false
);

CREATE TABLE IF NOT EXISTS likes
(
    id 		INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id  	INTEGER REFERENCES films (id),
    user_id  	INTEGER REFERENCES users (id)
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

