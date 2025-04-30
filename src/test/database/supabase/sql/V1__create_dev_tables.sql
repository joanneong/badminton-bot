CREATE TABLE IF NOT EXISTS dev_game (
    id              BIGSERIAL PRIMARY KEY,
    chat_id         BIGINT NOT NULL,
    date            DATE NOT NULL,
    start_time      TIME NOT NULL,
    end_time        TIME NOT NULL,
    location        VARCHAR(128) NOT NULL,
    max_players     INTEGER NOT NULL,
    price_per_pax   INTEGER NOT NULL,
    created_on      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_dev_game_chat_id ON dev_game(chat_id);

CREATE INDEX IF NOT EXISTS idx_dev_game_date ON dev_game(date);

CREATE TABLE IF NOT EXISTS dev_player (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(128) NOT NULL,
    game_id         BIGINT NOT NULL,
    created_on      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(game_id) REFERENCES dev_game(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_dev_player_game ON dev_player(game_id);

CREATE TABLE IF NOT EXISTS dev_court (
    id              BIGSERIAL PRIMARY KEY,
    game_id         BIGINT NOT NULL,
    court           VARCHAR(16) NOT NULL,
    created_on      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(game_id) REFERENCES dev_game(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_dev_court_game ON dev_court(game_id);
