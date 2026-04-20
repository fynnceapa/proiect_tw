SET search_path = project, pg_catalog;

DROP TABLE IF EXISTS book;

CREATE TABLE artists (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL,
    bio TEXT,
    image_url TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE genres (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);

CREATE TABLE albums (
    id UUID PRIMARY KEY,
    title TEXT NOT NULL,
    release_year INTEGER,
    cover_image_url TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    artist_id UUID NOT NULL,
    FOREIGN KEY (artist_id) REFERENCES project.artists (id) ON DELETE CASCADE
);

CREATE TABLE album_genre (
    album_id UUID NOT NULL,
    genre_id UUID NOT NULL,
    PRIMARY KEY (album_id, genre_id),
    FOREIGN KEY (album_id) REFERENCES project.albums (id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES project.genres (id) ON DELETE CASCADE
);

CREATE TABLE reviews (
    id UUID PRIMARY KEY,
    title TEXT NOT NULL,
    content TEXT NOT NULL,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 10),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    user_id UUID NOT NULL,
    album_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES project.users (id) ON DELETE CASCADE,
    FOREIGN KEY (album_id) REFERENCES project.albums (id) ON DELETE CASCADE
);

CREATE TABLE review_likes (
    user_id UUID NOT NULL,
    review_id UUID NOT NULL,
    PRIMARY KEY (user_id, review_id),
    FOREIGN KEY (user_id) REFERENCES project.users (id) ON DELETE CASCADE,
    FOREIGN KEY (review_id) REFERENCES project.reviews (id) ON DELETE CASCADE
);

CREATE TABLE comments (
    id UUID PRIMARY KEY,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    user_id UUID NOT NULL,
    review_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES project.users (id) ON DELETE CASCADE,
    FOREIGN KEY (review_id) REFERENCES project.reviews (id) ON DELETE CASCADE
);

CREATE TABLE user_profiles (
    id UUID PRIMARY KEY,
    display_name TEXT,
    bio TEXT,
    avatar_url TEXT,
    user_id UUID NOT NULL UNIQUE,
    FOREIGN KEY (user_id) REFERENCES project.users (id) ON DELETE CASCADE
);

CREATE TABLE user_follows (
    follower_id UUID NOT NULL,
    following_id UUID NOT NULL,
    PRIMARY KEY (follower_id, following_id),
    FOREIGN KEY (follower_id) REFERENCES project.users (id) ON DELETE CASCADE,
    FOREIGN KEY (following_id) REFERENCES project.users (id) ON DELETE CASCADE
);
