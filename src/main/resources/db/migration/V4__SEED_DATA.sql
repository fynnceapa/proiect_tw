SET search_path = project, pg_catalog;

-- Seed Artists
INSERT INTO artists (id, name, bio, image_url, created_at) VALUES
('a1a1a1a1-1111-1111-1111-111111111111', 'Radiohead', 'English rock band formed in Abingdon, Oxfordshire in 1985.', 'https://example.com/radiohead.jpg', NOW()),
('a2a2a2a2-2222-2222-2222-222222222222', 'Kendrick Lamar', 'American rapper and songwriter from Compton, California.', 'https://example.com/kendrick.jpg', NOW()),
('a3a3a3a3-3333-3333-3333-333333333333', 'Pink Floyd', 'English rock band formed in London in 1965.', 'https://example.com/pinkfloyd.jpg', NOW()),
('a4a4a4a4-4444-4444-4444-444444444444', 'Daft Punk', 'French electronic music duo formed in Paris in 1993.', 'https://example.com/daftpunk.jpg', NOW()),
('a5a5a5a5-5555-5555-5555-555555555555', 'Arctic Monkeys', 'English rock band formed in Sheffield in 2002.', 'https://example.com/arcticmonkeys.jpg', NOW());

-- Seed Genres
INSERT INTO genres (id, name) VALUES
('e1e1e1e1-1111-1111-1111-111111111111', 'Rock'),
('e2e2e2e2-2222-2222-2222-222222222222', 'Hip-Hop'),
('e3e3e3e3-3333-3333-3333-333333333333', 'Electronic'),
('e4e4e4e4-4444-4444-4444-444444444444', 'Alternative'),
('e5e5e5e5-5555-5555-5555-555555555555', 'Progressive Rock'),
('e6e6e6e6-6666-6666-6666-666666666666', 'Indie Rock');

-- Seed Albums
INSERT INTO albums (id, title, release_year, cover_image_url, created_at, artist_id) VALUES
('b1b1b1b1-1111-1111-1111-111111111111', 'OK Computer', 1997, 'https://example.com/okcomputer.jpg', NOW(), 'a1a1a1a1-1111-1111-1111-111111111111'),
('b2b2b2b2-2222-2222-2222-222222222222', 'Kid A', 2000, 'https://example.com/kida.jpg', NOW(), 'a1a1a1a1-1111-1111-1111-111111111111'),
('b3b3b3b3-3333-3333-3333-333333333333', 'To Pimp a Butterfly', 2015, 'https://example.com/tpab.jpg', NOW(), 'a2a2a2a2-2222-2222-2222-222222222222'),
('b4b4b4b4-4444-4444-4444-444444444444', 'good kid, m.A.A.d city', 2012, 'https://example.com/gkmc.jpg', NOW(), 'a2a2a2a2-2222-2222-2222-222222222222'),
('b5b5b5b5-5555-5555-5555-555555555555', 'The Dark Side of the Moon', 1973, 'https://example.com/dsotm.jpg', NOW(), 'a3a3a3a3-3333-3333-3333-333333333333'),
('b6b6b6b6-6666-6666-6666-666666666666', 'Wish You Were Here', 1975, 'https://example.com/wywh.jpg', NOW(), 'a3a3a3a3-3333-3333-3333-333333333333'),
('b7b7b7b7-7777-7777-7777-777777777777', 'Discovery', 2001, 'https://example.com/discovery.jpg', NOW(), 'a4a4a4a4-4444-4444-4444-444444444444'),
('b8b8b8b8-8888-8888-8888-888888888888', 'Random Access Memories', 2013, 'https://example.com/ram.jpg', NOW(), 'a4a4a4a4-4444-4444-4444-444444444444'),
('b9b9b9b9-9999-9999-9999-999999999999', 'Whatever People Say I Am, That''s What I''m Not', 2006, 'https://example.com/wpsa.jpg', NOW(), 'a5a5a5a5-5555-5555-5555-555555555555'),
('b0b0b0b0-0000-0000-0000-000000000000', 'AM', 2013, 'https://example.com/am.jpg', NOW(), 'a5a5a5a5-5555-5555-5555-555555555555');

-- Seed Album-Genre associations
INSERT INTO album_genre (album_id, genre_id) VALUES
('b1b1b1b1-1111-1111-1111-111111111111', 'e1e1e1e1-1111-1111-1111-111111111111'), -- OK Computer -> Rock
('b1b1b1b1-1111-1111-1111-111111111111', 'e4e4e4e4-4444-4444-4444-444444444444'), -- OK Computer -> Alternative
('b2b2b2b2-2222-2222-2222-222222222222', 'e3e3e3e3-3333-3333-3333-333333333333'), -- Kid A -> Electronic
('b2b2b2b2-2222-2222-2222-222222222222', 'e4e4e4e4-4444-4444-4444-444444444444'), -- Kid A -> Alternative
('b3b3b3b3-3333-3333-3333-333333333333', 'e2e2e2e2-2222-2222-2222-222222222222'), -- TPAB -> Hip-Hop
('b4b4b4b4-4444-4444-4444-444444444444', 'e2e2e2e2-2222-2222-2222-222222222222'), -- GKMC -> Hip-Hop
('b5b5b5b5-5555-5555-5555-555555555555', 'e5e5e5e5-5555-5555-5555-555555555555'), -- DSOTM -> Progressive Rock
('b5b5b5b5-5555-5555-5555-555555555555', 'e1e1e1e1-1111-1111-1111-111111111111'), -- DSOTM -> Rock
('b6b6b6b6-6666-6666-6666-666666666666', 'e5e5e5e5-5555-5555-5555-555555555555'), -- WYWH -> Progressive Rock
('b7b7b7b7-7777-7777-7777-777777777777', 'e3e3e3e3-3333-3333-3333-333333333333'), -- Discovery -> Electronic
('b8b8b8b8-8888-8888-8888-888888888888', 'e3e3e3e3-3333-3333-3333-333333333333'), -- RAM -> Electronic
('b9b9b9b9-9999-9999-9999-999999999999', 'e6e6e6e6-6666-6666-6666-666666666666'), -- WPSA -> Indie Rock
('b0b0b0b0-0000-0000-0000-000000000000', 'e6e6e6e6-6666-6666-6666-666666666666'), -- AM -> Indie Rock
('b0b0b0b0-0000-0000-0000-000000000000', 'e1e1e1e1-1111-1111-1111-111111111111'); -- AM -> Rock

-- Seed a test user (password is "password123" encoded with bcrypt)
INSERT INTO users (id, username, email, password) VALUES
('d1d1d1d1-1111-1111-1111-111111111111', 'musicfan', 'musicfan@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
('d2d2d2d2-2222-2222-2222-222222222222', 'reviewer42', 'reviewer42@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');

-- Give them USER role
INSERT INTO user_role (user_id, role_id) VALUES
('d1d1d1d1-1111-1111-1111-111111111111', 2),
('d2d2d2d2-2222-2222-2222-222222222222', 2);

-- Seed User Profiles
INSERT INTO user_profiles (id, display_name, bio, avatar_url, user_id) VALUES
('f1f1f1f1-1111-1111-1111-111111111111', 'Music Fan', 'I love discovering new music and sharing my thoughts!', 'https://example.com/avatar1.jpg', 'd1d1d1d1-1111-1111-1111-111111111111'),
('f2f2f2f2-2222-2222-2222-222222222222', 'The Reviewer', 'Professional music critic. Album nerd.', 'https://example.com/avatar2.jpg', 'd2d2d2d2-2222-2222-2222-222222222222');

-- Seed Reviews
INSERT INTO reviews (id, title, content, rating, created_at, updated_at, user_id, album_id) VALUES
('1f1f1f1f-1111-1111-1111-111111111111', 'A Masterpiece of Modern Rock', 'OK Computer is a landmark album that redefined what rock music could be. Every track is perfectly crafted.', 10, NOW(), NOW(), 'd1d1d1d1-1111-1111-1111-111111111111', 'b1b1b1b1-1111-1111-1111-111111111111'),
('2f2f2f2f-2222-2222-2222-222222222222', 'Bold and Experimental', 'Kid A was polarizing on release but has aged beautifully. An incredible electronic reinvention.', 9, NOW(), NOW(), 'd1d1d1d1-1111-1111-1111-111111111111', 'b2b2b2b2-2222-2222-2222-222222222222'),
('3f3f3f3f-3333-3333-3333-333333333333', 'Hip-Hop at Its Peak', 'TPAB is a cultural monument. The jazz-infused production and lyrical depth are unmatched.', 10, NOW(), NOW(), 'd2d2d2d2-2222-2222-2222-222222222222', 'b3b3b3b3-3333-3333-3333-333333333333'),
('4f4f4f4f-4444-4444-4444-444444444444', 'Timeless Classic', 'The Dark Side of the Moon is a sonic journey that transcends genre boundaries.', 10, NOW(), NOW(), 'd2d2d2d2-2222-2222-2222-222222222222', 'b5b5b5b5-5555-5555-5555-555555555555'),
('5f5f5f5f-5555-5555-5555-555555555555', 'Feel-Good Electronic Perfection', 'Discovery is pure joy in album form. Every song is a banger.', 9, NOW(), NOW(), 'd1d1d1d1-1111-1111-1111-111111111111', 'b7b7b7b7-7777-7777-7777-777777777777');

-- Seed Review Likes
INSERT INTO review_likes (user_id, review_id) VALUES
('d2d2d2d2-2222-2222-2222-222222222222', '1f1f1f1f-1111-1111-1111-111111111111'),
('d2d2d2d2-2222-2222-2222-222222222222', '2f2f2f2f-2222-2222-2222-222222222222'),
('d1d1d1d1-1111-1111-1111-111111111111', '3f3f3f3f-3333-3333-3333-333333333333'),
('d1d1d1d1-1111-1111-1111-111111111111', '4f4f4f4f-4444-4444-4444-444444444444');

-- Seed Comments
INSERT INTO comments (id, content, created_at, user_id, review_id) VALUES
('c1c1c1c1-1111-1111-1111-111111111111', 'Totally agree! OK Computer changed my life.', NOW(), 'd2d2d2d2-2222-2222-2222-222222222222', '1f1f1f1f-1111-1111-1111-111111111111'),
('c2c2c2c2-2222-2222-2222-222222222222', 'Great review! TPAB deserves every bit of praise.', NOW(), 'd1d1d1d1-1111-1111-1111-111111111111', '3f3f3f3f-3333-3333-3333-333333333333'),
('c3c3c3c3-3333-3333-3333-333333333333', 'I prefer The Wall, but DSOTM is also incredible.', NOW(), 'd1d1d1d1-1111-1111-1111-111111111111', '4f4f4f4f-4444-4444-4444-444444444444');

-- Seed User Follows (musicfan follows reviewer42 and vice versa)
INSERT INTO user_follows (follower_id, following_id) VALUES
('d1d1d1d1-1111-1111-1111-111111111111', 'd2d2d2d2-2222-2222-2222-222222222222'),
('d2d2d2d2-2222-2222-2222-222222222222', 'd1d1d1d1-1111-1111-1111-111111111111');
