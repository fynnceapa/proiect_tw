SET search_path = project, pg_catalog;

INSERT INTO artists (id, name, bio, image_url, created_at) VALUES
('a6a6a6a6-6666-6666-6666-666666666666', 'Nirvana', 'American rock band formed in Aberdeen, Washington in 1987.', 'https://example.com/nirvana.jpg', NOW()),
('a7a7a7a7-7777-7777-7777-777777777777', 'The Beatles', 'English rock band formed in Liverpool in 1960.', 'https://example.com/beatles.jpg', NOW()),
('a8a8a8a8-8888-8888-8888-888888888888', 'Led Zeppelin', 'English rock band formed in London in 1968.', 'https://example.com/ledzeppelin.jpg', NOW()),
('a9a9a9a9-9999-9999-9999-999999999999', 'OutKast', 'American hip-hop duo from Atlanta, Georgia.', 'https://example.com/outkast.jpg', NOW()),
('aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1', 'Wu-Tang Clan', 'American hip-hop collective formed in Staten Island in 1992.', 'https://example.com/wutang.jpg', NOW()),
('bbbbbbb2-bbbb-bbbb-bbbb-bbbbbbbbbbb2', 'The Strokes', 'American rock band formed in New York City in 1998.', 'https://example.com/strokes.jpg', NOW()),
('ccccccc3-cccc-cccc-cccc-ccccccccccc3', 'Nas', 'American rapper from Queens, New York.', 'https://example.com/nas.jpg', NOW());

INSERT INTO albums (id, title, release_year, cover_image_url, created_at, artist_id) VALUES
('c1010101-0101-0101-0101-010101010101', 'Nevermind', 1991, 'https://upload.wikimedia.org/wikipedia/en/b/b7/NirvanaNevermindalbumcover.jpg', NOW(), 'a6a6a6a6-6666-6666-6666-666666666666'),
('c2020202-0202-0202-0202-020202020202', 'In Utero', 1993, 'https://upload.wikimedia.org/wikipedia/en/e/e5/In_Utero_%28Nirvana%29_album_cover.jpg', NOW(), 'a6a6a6a6-6666-6666-6666-666666666666'),
('c3030303-0303-0303-0303-030303030303', 'Abbey Road', 1969, 'https://upload.wikimedia.org/wikipedia/commons/a/a4/The_Beatles_Abbey_Road_album_cover.jpg', NOW(), 'a7a7a7a7-7777-7777-7777-777777777777'),
('c4040404-0404-0404-0404-040404040404', 'Revolver', 1966, 'https://upload.wikimedia.org/wikipedia/en/e/ec/Revolver_%28album_cover%29.jpg', NOW(), 'a7a7a7a7-7777-7777-7777-777777777777'),
('c6060606-0606-0606-0606-060606060606', 'Is This It', 2001, 'https://upload.wikimedia.org/wikipedia/en/thumb/e/e7/The_Strokes_-_Ist_Tis_It_US_cover.png/250px-The_Strokes_-_Ist_Tis_It_US_cover.png', NOW(), 'bbbbbbb2-bbbb-bbbb-bbbb-bbbbbbbbbbb2'),
('c7070707-0707-0707-0707-070707070707', 'Stankonia', 2000, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcStof6CBXBX18Syow4Kx_Ceh03hbt5m-vS3Sw&s', NOW(), 'a9a9a9a9-9999-9999-9999-999999999999'),
('c8080808-0808-0808-0808-080808080808', 'Speakerboxxx/The Love Below', 2003, 'https://upload.wikimedia.org/wikipedia/en/5/54/Speakerboxxx-The_Love_Below.png', NOW(), 'a9a9a9a9-9999-9999-9999-999999999999'),
('c9090909-0909-0909-0909-090909090909', 'Enter the Wu-Tang (36 Chambers)', 1993, 'https://upload.wikimedia.org/wikipedia/en/5/53/Wu-TangClanEntertheWu-Tangalbumcover.jpg', NOW(), 'aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1'),
('c0a0a0a0-0a0a-0a0a-0a0a-0a0a0a0a0a0a', 'Illmatic', 1994, 'https://upload.wikimedia.org/wikipedia/en/2/27/IllmaticNas.jpg', NOW(), 'ccccccc3-cccc-cccc-cccc-ccccccccccc3');

INSERT INTO album_genre (album_id, genre_id) VALUES
('c1010101-0101-0101-0101-010101010101', 'e1e1e1e1-1111-1111-1111-111111111111'),
('c1010101-0101-0101-0101-010101010101', 'e4e4e4e4-4444-4444-4444-444444444444'),
('c2020202-0202-0202-0202-020202020202', 'e1e1e1e1-1111-1111-1111-111111111111'),
('c2020202-0202-0202-0202-020202020202', 'e4e4e4e4-4444-4444-4444-444444444444'),
('c3030303-0303-0303-0303-030303030303', 'e1e1e1e1-1111-1111-1111-111111111111'),
('c4040404-0404-0404-0404-040404040404', 'e1e1e1e1-1111-1111-1111-111111111111'),
('c6060606-0606-0606-0606-060606060606', 'e6e6e6e6-6666-6666-6666-666666666666'),
('c6060606-0606-0606-0606-060606060606', 'e1e1e1e1-1111-1111-1111-111111111111'),
('c7070707-0707-0707-0707-070707070707', 'e2e2e2e2-2222-2222-2222-222222222222'),
('c8080808-0808-0808-0808-080808080808', 'e2e2e2e2-2222-2222-2222-222222222222'),
('c9090909-0909-0909-0909-090909090909', 'e2e2e2e2-2222-2222-2222-222222222222'),
('c0a0a0a0-0a0a-0a0a-0a0a-0a0a0a0a0a0a', 'e2e2e2e2-2222-2222-2222-222222222222');

INSERT INTO users (id, username, email, password) VALUES
('d3d3d3d3-3333-3333-3333-333333333333', 'vinylvibes', 'vinylvibes@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
('d4d4d4d4-4444-4444-4444-444444444444', 'bassline', 'bassline@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
('d5d5d5d5-5555-5555-5555-555555555555', 'dustyloop', 'dustyloop@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
('d6d6d6d6-6666-6666-6666-666666666666', 'cratehunter', 'cratehunter@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
('d7d7d7d7-7777-7777-7777-777777777777', 'boombapkid', 'boombapkid@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');

INSERT INTO user_role (user_id, role_id) VALUES
('d3d3d3d3-3333-3333-3333-333333333333', 2),
('d4d4d4d4-4444-4444-4444-444444444444', 2),
('d5d5d5d5-5555-5555-5555-555555555555', 2),
('d6d6d6d6-6666-6666-6666-666666666666', 2),
('d7d7d7d7-7777-7777-7777-777777777777', 2);

INSERT INTO user_profiles (id, display_name, bio, avatar_url, user_id) VALUES
('f3f3f3f3-3333-3333-3333-333333333333', 'Vinyl Vibes', 'Always digging for rare pressings and hidden gems.', 'https://example.com/avatar3.jpg', 'd3d3d3d3-3333-3333-3333-333333333333'),
('f4f4f4f4-4444-4444-4444-444444444444', 'Bassline', 'Low-end lover and groove collector.', 'https://example.com/avatar4.jpg', 'd4d4d4d4-4444-4444-4444-444444444444'),
('f5f5f5f5-5555-5555-5555-555555555555', 'Dusty Loop', 'Sampling culture and classic hip-hop head.', 'https://example.com/avatar5.jpg', 'd5d5d5d5-5555-5555-5555-555555555555'),
('f6f6f6f6-6666-6666-6666-666666666666', 'Crate Hunter', 'Finding deep cuts one record at a time.', 'https://example.com/avatar6.jpg', 'd6d6d6d6-6666-6666-6666-666666666666'),
('f7f7f7f7-7777-7777-7777-777777777777', 'Boom Bap Kid', '90s hip-hop, breakbeats, and boom bap forever.', 'https://example.com/avatar7.jpg', 'd7d7d7d7-7777-7777-7777-777777777777');
