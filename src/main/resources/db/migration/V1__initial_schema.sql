CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ENUMS
CREATE TYPE session_category AS ENUM ('FOOD', 'ACTIVITY', 'MOVIE', 'TASK', 'CUSTOM');
CREATE TYPE session_status AS ENUM ('ACTIVE','CLOSED', 'ARCHIVED');
CREATE TYPE vote_mode AS ENUM ('DEFAULT', 'POINTS', 'RANKED', 'VETO');

-- USERS TABLE
CREATE TABLE users(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email TEXT UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    email_verified BOOLEAN DEFAULT FALSE NOT NULL,
    is_active BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX idx_users_email ON users(email);

-- USER PROFILES TABLE
CREATE TABLE user_profiles(
    user_id UUID PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    username TEXT UNIQUE NOT NULL,
    sessions_joined INTEGER DEFAULT 0 NOT NULL,
    sessions_hosted INTEGER DEFAULT 0 NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT username_length CHECK (char_length(username) BETWEEN 3 and 30),
    CONSTRAINT username_format CHECK (username ~ '^[a-zA-Z0-9_]+$')
);

CREATE UNIQUE INDEX idx_user_profiles_username ON user_profiles(username);

-- SESSIONS TABLE
CREATE TABLE sessions(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    created_by UUID UNIQUE NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    session_code CHAR(6) UNIQUE NOT NULL,
    title TEXT NOT NULL,
    description TEXT,
    category session_category NOT NULL,
    voting_mode vote_mode NOT NULL,
    status session_status DEFAULT 'ACTIVE' NOT NULL,
    max_participants INTEGER DEFAULT 10 NOT NULL,
    allow_participant_options BOOLEAN DEFAULT TRUE NOT NULL,
    anonymous_voting BOOLEAN DEFAULT FALSE NOT NULL,
    show_live_results BOOLEAN DEFAULT TRUE NOT NULL,
    duration INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    closed_at TIMESTAMP

    CONSTRAINT valid_session_code CHECK (session_code ~ '^[A-Z0-9]{6}$'),
    CONSTRAINT valid_max_participants CHECK (max_participants BETWEEN 2 AND 50),
    CONSTRAINT valid_time_limit CHECK (
        duration IS NULL OR
        duration BETWEEN 5 AND 1440
    )
);

CREATE UNIQUE INDEX idx_sessions_code ON sessions(session_code);
CREATE INDEX idx_sessions_creator ON sessions(created_by);
CREATE INDEX idx_sessions_status ON sessions(status);
CREATE INDEX idx_sessions_created_at ON sessions(created_at DESC);
CREATE INDEX idx_sessions_auto_close ON sessions(created_at, duration)
    WHERE status = 'ACTIVE' AND duration IS NOT NULL;

-- SESSION OPTIONS TABLE
CREATE TABLE session_options(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id UUID UNIQUE NOT NULL REFERENCES sessions(id) ON DELETE CASCADE,
    created_by UUID UNIQUE NOT NULL REFERENCES users(id) ON DELETE SET NULL,
    option_text TEXT NOT NULL,
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_option_per_session UNIQUE(session_id, option_text)
);

CREATE INDEX idx_session_options_session ON session_options(session_id);

-- SESSION PARTICIPANTS TABLE
CREATE TABLE session_participants (
    session_id UUID NOT NULL REFERENCES sessions(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    has_voted BOOLEAN DEFAULT FALSE NOT NULL,
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (session_id, user_id)
);

CREATE INDEX idx_participants_session ON session_participants(session_id);
CREATE INDEX idx_participants_user ON session_participants(user_id);
CREATE INDEX idx_participants_session_voted ON session_participants(session_id, has_voted);

-- VOTES TABLE
CREATE TABLE votes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id UUID NOT NULL REFERENCES sessions(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    option_id UUID NOT NULL REFERENCES session_options(id) ON DELETE CASCADE,
    vote_value INTEGER NOT NULL,
    is_veto BOOLEAN DEFAULT FALSE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT vote_value_non_negative CHECK (vote_value >= 0)
);

CREATE INDEX idx_votes_session ON votes(session_id);
CREATE INDEX idx_votes_session_option ON votes(session_id, option_id) WHERE is_veto = FALSE;
CREATE UNIQUE INDEX idx_votes_unique_regular ON votes(session_id, user_id, option_id) WHERE is_veto = FALSE;
CREATE UNIQUE INDEX idx_votes_unique_veto ON votes(session_id, user_id, option_id) WHERE is_veto = TRUE;
CREATE INDEX idx_votes_session_user ON votes(session_id, user_id);

-- SESSION RESULTS TABLE
CREATE TABLE session_results (
    session_id UUID PRIMARY KEY NOT NULL REFERENCES sessions(id) ON DELETE CASCADE,
    winner_option_id UUID REFERENCES session_options(id) ON DELETE SET NULL,
    winner_option_text VARCHAR(255) NOT NULL,
    winner_score INTEGER NOT NULL,
    full_results JSONB NOT NULL,
    total_votes_cast INTEGER NOT NULL,
    total_participants INTEGER NOT NULL
);

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_user_profiles_updated_at
    BEFORE UPDATE ON user_profiles
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();
