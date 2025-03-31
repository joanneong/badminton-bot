CREATE TABLE IF NOT EXISTS court (
    id              BIGSERIAL PRIMARY KEY,
    game_id         INTEGER,
    court           VARCHAR(16),
    created_on      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(game_id) REFERENCES game(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_court_game ON court(game_id);
