-- SAMPLE USERS (Generated)
INSERT INTO users (id, email, password_hash, email_verified, is_active, created_at, updated_at)
VALUES
    ('550e8400-e29b-41d4-a716-446655440000', 'alice@example.com', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYCj.xjRZ4K', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440001', 'bob@example.com', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYCj.xjRZ4K', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440002', 'charlie@example.com', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYCj.xjRZ4K', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440003', 'diana@example.com', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYCj.xjRZ4K', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440004', 'evan@example.com', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYCj.xjRZ4K', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- SAMPLE USER PROFILES (GENERATED)
INSERT INTO user_profiles (user_id, username, sessions_joined, sessions_hosted, created_at, updated_at)
VALUES
    ('550e8400-e29b-41d4-a716-446655440000', 'alice_dev', 5, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440001', 'bob_dev', 4, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440002', 'charlie_dev', 3, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440003', 'diana_dev', 2, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440004', 'evan_dev', 1, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- SAMPLE SESSIONS (GENERATED)
-- Session 1: Active POINTS mode session
INSERT INTO sessions (id, session_code, title, description, category, voting_mode, status, max_participants, allow_participant_options, anonymous_voting, show_live_results, duration, created_by, created_at)
VALUES
    ('650e8400-e29b-41d4-a716-446655440000', 'ABC123', 'Friday Lunch Spot', 'Let''s decide where to eat for Friday lunch!', 'FOOD', 'POINTS', 'ACTIVE', 20, true, false, true, 30, '550e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP - INTERVAL '5 minutes');

-- Session 2: Active RANKED mode session
INSERT INTO sessions (id, session_code, title, description, category, voting_mode, status, max_participants, allow_participant_options, anonymous_voting, show_live_results, duration, created_by, created_at)
VALUES
    ('650e8400-e29b-41d4-a716-446655440001', 'XYZ789', 'Movie Night Pick', 'Vote for tonight''s movie', 'MOVIE', 'RANKED', 'ACTIVE', 10, false, false, true, NULL, '550e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP - INTERVAL '10 minutes');

-- Session 3: Closed session with results
INSERT INTO sessions (id, session_code, title, description, category, voting_mode, status, max_participants, allow_participant_options, anonymous_voting, show_live_results, duration, created_by, created_at, closed_at)
VALUES
    ('650e8400-e29b-41d4-a716-446655440002', 'DEF456', 'Team Building Activity', 'What should we do for team building?', 'ACTIVITY', 'DEFAULT', 'CLOSED', 15, true, false, true, 60, '550e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP - INTERVAL '2 hours', CURRENT_TIMESTAMP - INTERVAL '1 hour');

-- Session 4: Active VETO mode session
INSERT INTO sessions (id, session_code, title, description, category, voting_mode, status, max_participants, allow_participant_options, anonymous_voting, show_live_results, duration, created_by, created_at)
VALUES
    ('650e8400-e29b-41d4-a716-446655440003', 'VET123', 'Project Name Vote', 'Choose the new project name', 'TASK', 'VETO', 'ACTIVE', 20, true, true, false, 120, '550e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP - INTERVAL '15 minutes');

-- Sample Session Options for Session 1 (Friday Lunch)
INSERT INTO session_options (id, session_id, option_text, metadata, created_by, created_at)
VALUES
    ('750e8400-e29b-41d4-a716-446655440000', '650e8400-e29b-41d4-a716-446655440000', 'Sushi House', '{"cuisine": "Japanese", "priceRange": "$$", "distance": "0.5 miles"}', '550e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP - INTERVAL '5 minutes'),
    ('750e8400-e29b-41d4-a716-446655440001', '650e8400-e29b-41d4-a716-446655440000', 'Tacos El Rey', '{"cuisine": "Mexican", "priceRange": "$", "distance": "0.3 miles"}', '550e8400-e29b-41d4-a716-446655440000',  CURRENT_TIMESTAMP - INTERVAL '5 minutes'),
    ('750e8400-e29b-41d4-a716-446655440002', '650e8400-e29b-41d4-a716-446655440000', 'Pizza Palace', '{"cuisine": "Italian", "priceRange": "$$", "distance": "0.8 miles"}', '550e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP - INTERVAL '4 minutes'),
    ('750e8400-e29b-41d4-a716-446655440003', '650e8400-e29b-41d4-a716-446655440000', 'Thai Garden', '{"cuisine": "Thai", "priceRange": "$$", "distance": "1.2 miles"}', '550e8400-e29b-41d4-a716-446655440002', CURRENT_TIMESTAMP - INTERVAL '3 minutes');

-- Sample Session Options for Session 2 (Movie Night)
INSERT INTO session_options (id, session_id, option_text, metadata, created_by, created_at)
VALUES
    ('750e8400-e29b-41d4-a716-446655440010', '650e8400-e29b-41d4-a716-446655440001', 'Dune: Part Two', '{"genre": "Sci-Fi", "runtime": 166, "rating": "PG-13"}', '550e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP - INTERVAL '10 minutes'),
    ('750e8400-e29b-41d4-a716-446655440011', '650e8400-e29b-41d4-a716-446655440001', 'Oppenheimer', '{"genre": "Biography", "runtime": 180, "rating": "R"}', '550e8400-e29b-41d4-a716-446655440001',  CURRENT_TIMESTAMP - INTERVAL '10 minutes'),
    ('750e8400-e29b-41d4-a716-446655440012', '650e8400-e29b-41d4-a716-446655440001', 'Poor Things', '{"genre": "Comedy", "runtime": 141, "rating": "R"}', '550e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP - INTERVAL '10 minutes');

-- Sample Session Options for Session 3 (Team Building - Closed)
INSERT INTO session_options (id, session_id, option_text, metadata, created_by, created_at)
VALUES
    ('750e8400-e29b-41d4-a716-446655440020', '650e8400-e29b-41d4-a716-446655440002', 'Escape Room', '{"indoor": true, "cost": 30, "duration": 60}', '550e8400-e29b-41d4-a716-446655440000',  CURRENT_TIMESTAMP - INTERVAL '2 hours'),
    ('750e8400-e29b-41d4-a716-446655440021', '650e8400-e29b-41d4-a716-446655440002', 'Bowling', '{"indoor": true, "cost": 20, "duration": 120}', '550e8400-e29b-41d4-a716-446655440000',  CURRENT_TIMESTAMP - INTERVAL '2 hours'),
    ('750e8400-e29b-41d4-a716-446655440022', '650e8400-e29b-41d4-a716-446655440002', 'Hiking', '{"indoor": false, "cost": 0, "duration": 180}', '550e8400-e29b-41d4-a716-446655440001',  CURRENT_TIMESTAMP - INTERVAL '2 hours');

-- Sample Session Options for Session 4 (Project Name)
INSERT INTO session_options (id, session_id, option_text, metadata, created_by, created_at)
VALUES
    ('750e8400-e29b-41d4-a716-446655440030', '650e8400-e29b-41d4-a716-446655440003', 'Project Phoenix', '{"category": "mythological"}', '550e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP - INTERVAL '15 minutes'),
    ('750e8400-e29b-41d4-a716-446655440031', '650e8400-e29b-41d4-a716-446655440003', 'Project Titan', '{"category": "mythological"}', '550e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP - INTERVAL '15 minutes'),
    ('750e8400-e29b-41d4-a716-446655440032', '650e8400-e29b-41d4-a716-446655440003', 'Project Nova', '{"category": "astronomical"}', '550e8400-e29b-41d4-a716-446655440002', CURRENT_TIMESTAMP - INTERVAL '14 minutes');

-- Sample Participants for Session 1 (Friday Lunch)
INSERT INTO session_participants (session_id, user_id, has_voted, joined_at)
VALUES
    ('650e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440000', true, CURRENT_TIMESTAMP - INTERVAL '5 minutes'),
    ('650e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440001', true, CURRENT_TIMESTAMP - INTERVAL '4 minutes'),
    ('650e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440002', false, CURRENT_TIMESTAMP - INTERVAL '3 minutes');

-- Sample Participants for Session 2 (Movie Night)
INSERT INTO session_participants (session_id, user_id, has_voted, joined_at)
VALUES
    ('650e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440001', true, CURRENT_TIMESTAMP - INTERVAL '10 minutes'),
    ('650e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440002', true, CURRENT_TIMESTAMP - INTERVAL '9 minutes'),
    ('650e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440003', false, CURRENT_TIMESTAMP - INTERVAL '8 minutes');

-- Sample Participants for Session 3 (Team Building - Closed)
INSERT INTO session_participants (session_id, user_id, has_voted, joined_at)
VALUES
    ('650e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440000', true, CURRENT_TIMESTAMP - INTERVAL '2 hours'),
    ('650e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440001', true, CURRENT_TIMESTAMP - INTERVAL '2 hours'),
    ('650e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440002', true, CURRENT_TIMESTAMP - INTERVAL '110 minutes');

-- Sample Participants for Session 4 (Project Name)
INSERT INTO session_participants (session_id, user_id, has_voted, joined_at)
VALUES
    ('650e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440001', false, CURRENT_TIMESTAMP - INTERVAL '15 minutes'),
    ('650e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440002', false, CURRENT_TIMESTAMP - INTERVAL '14 minutes'),
    ('650e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440003', false, CURRENT_TIMESTAMP - INTERVAL '13 minutes');

-- Sample Votes for Session 1 (POINTS mode - 10 points distributed)
INSERT INTO votes (id, session_id, user_id, option_id, vote_value, is_veto, created_at)
VALUES
    -- Alice's votes (10 points total)
    ('850e8400-e29b-41d4-a716-446655440000', '650e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440000', '750e8400-e29b-41d4-a716-446655440000', 4, false, CURRENT_TIMESTAMP - INTERVAL '2 minutes'),
    ('850e8400-e29b-41d4-a716-446655440001', '650e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440000', '750e8400-e29b-41d4-a716-446655440001', 3, false, CURRENT_TIMESTAMP - INTERVAL '2 minutes'),
    ('850e8400-e29b-41d4-a716-446655440002', '650e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440000', '750e8400-e29b-41d4-a716-446655440002', 2, false, CURRENT_TIMESTAMP - INTERVAL '2 minutes'),
    ('850e8400-e29b-41d4-a716-446655440003', '650e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440000', '750e8400-e29b-41d4-a716-446655440003', 1, false, CURRENT_TIMESTAMP - INTERVAL '2 minutes'),
    -- Bob's votes (10 points total)
    ('850e8400-e29b-41d4-a716-446655440010', '650e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440001', '750e8400-e29b-41d4-a716-446655440001', 5, false, CURRENT_TIMESTAMP - INTERVAL '3 minutes'),
    ('850e8400-e29b-41d4-a716-446655440011', '650e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440001', '750e8400-e29b-41d4-a716-446655440000', 3, false, CURRENT_TIMESTAMP - INTERVAL '3 minutes'),
    ('850e8400-e29b-41d4-a716-446655440012', '650e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440001', '750e8400-e29b-41d4-a716-446655440002', 2, false, CURRENT_TIMESTAMP - INTERVAL '3 minutes');

-- Sample Votes for Session 2 (RANKED mode - rank 1, 2, 3)
INSERT INTO votes (id, session_id, user_id, option_id, vote_value, is_veto, created_at)
VALUES
    -- Bob's rankings
    ('850e8400-e29b-41d4-a716-446655440020', '650e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440001', '750e8400-e29b-41d4-a716-446655440010', 1, false, CURRENT_TIMESTAMP - INTERVAL '8 minutes'),
    ('850e8400-e29b-41d4-a716-446655440021', '650e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440001', '750e8400-e29b-41d4-a716-446655440011', 2, false, CURRENT_TIMESTAMP - INTERVAL '8 minutes'),
    ('850e8400-e29b-41d4-a716-446655440022', '650e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440001', '750e8400-e29b-41d4-a716-446655440012', 3, false, CURRENT_TIMESTAMP - INTERVAL '8 minutes'),
    -- Charlie's rankings
    ('850e8400-e29b-41d4-a716-446655440023', '650e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440002', '750e8400-e29b-41d4-a716-446655440011', 1, false, CURRENT_TIMESTAMP - INTERVAL '7 minutes'),
    ('850e8400-e29b-41d4-a716-446655440024', '650e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440002', '750e8400-e29b-41d4-a716-446655440010', 2, false, CURRENT_TIMESTAMP - INTERVAL '7 minutes'),
    ('850e8400-e29b-41d4-a716-446655440025', '650e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440002', '750e8400-e29b-41d4-a716-446655440012', 3, false, CURRENT_TIMESTAMP - INTERVAL '7 minutes');

-- Sample Votes for Session 3 (DEFAULT mode - simple yes/no, 1 or 0)
INSERT INTO votes (id, session_id, user_id, option_id, vote_value, is_veto, created_at)
VALUES
    -- Alice's votes
    ('850e8400-e29b-41d4-a716-446655440030', '650e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440000', '750e8400-e29b-41d4-a716-446655440020', 1, false, CURRENT_TIMESTAMP - INTERVAL '90 minutes'),
    ('850e8400-e29b-41d4-a716-446655440031', '650e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440000', '750e8400-e29b-41d4-a716-446655440021', 0, false, CURRENT_TIMESTAMP - INTERVAL '90 minutes'),
    ('850e8400-e29b-41d4-a716-446655440032', '650e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440000', '750e8400-e29b-41d4-a716-446655440022', 0, false, CURRENT_TIMESTAMP - INTERVAL '90 minutes'),
    -- Bob's votes
    ('850e8400-e29b-41d4-a716-446655440033', '650e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440001', '750e8400-e29b-41d4-a716-446655440020', 1, false, CURRENT_TIMESTAMP - INTERVAL '90 minutes'),
    ('850e8400-e29b-41d4-a716-446655440034', '650e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440001', '750e8400-e29b-41d4-a716-446655440021', 1, false, CURRENT_TIMESTAMP - INTERVAL '90 minutes'),
    ('850e8400-e29b-41d4-a716-446655440035', '650e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440001', '750e8400-e29b-41d4-a716-446655440022', 0, false, CURRENT_TIMESTAMP - INTERVAL '90 minutes'),
    -- Charlie's votes
    ('850e8400-e29b-41d4-a716-446655440036', '650e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440002', '750e8400-e29b-41d4-a716-446655440020', 1, false, CURRENT_TIMESTAMP - INTERVAL '95 minutes'),
    ('850e8400-e29b-41d4-a716-446655440037', '650e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440002', '750e8400-e29b-41d4-a716-446655440021', 0, false, CURRENT_TIMESTAMP - INTERVAL '95 minutes'),
    ('850e8400-e29b-41d4-a716-446655440038', '650e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440002', '750e8400-e29b-41d4-a716-446655440022', 1, false, CURRENT_TIMESTAMP - INTERVAL '95 minutes');

-- Sample Result for Session 3 (Closed session)
INSERT INTO session_results (session_id, winner_option_id, winner_option_text, winner_score, full_results, total_votes_cast, total_participants)
VALUES (
           '650e8400-e29b-41d4-a716-446655440002',
           '750e8400-e29b-41d4-a716-446655440020',
           'Escape Room',
           3,
           '{
               "rankings": [
                   {"rank": 1, "optionId": "750e8400-e29b-41d4-a716-446655440020", "optionText": "Escape Room", "score": 3, "voteCount": 3, "percentage": 100},
                   {"rank": 2, "optionId": "750e8400-e29b-41d4-a716-446655440021", "optionText": "Bowling", "score": 1, "voteCount": 3, "percentage": 33.3},
                   {"rank": 3, "optionId": "750e8400-e29b-41d4-a716-446655440022", "optionText": "Hiking", "score": 1, "voteCount": 3, "percentage": 33.3}
               ],
               "breakdown": [
                   {"userId": "550e8400-e29b-41d4-a716-446655440000", "username": "alice_dev", "votes": [{"optionId": "750e8400-e29b-41d4-a716-446655440020", "value": 1}]},
                   {"userId": "550e8400-e29b-41d4-a716-446655440001", "username": "bob_dev", "votes": [{"optionId": "750e8400-e29b-41d4-a716-446655440020", "value": 1}, {"optionId": "750e8400-e29b-41d4-a716-446655440021", "value": 1}]},
                   {"userId": "550e8400-e29b-41d4-a716-446655440002", "username": "charlie_dev", "votes": [{"optionId": "750e8400-e29b-41d4-a716-446655440020", "value": 1}, {"optionId": "750e8400-e29b-41d4-a716-446655440022", "value": 1}]}
               ]
           }',
           9,
           3
       );