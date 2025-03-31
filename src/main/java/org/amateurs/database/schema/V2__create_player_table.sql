CREATE TABLE IF NOT EXISTS player (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(128) NOT NULL,
    game_id         INTEGER,
    created_on      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(game_id) REFERENCES game(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_player_game ON player(game_id);
