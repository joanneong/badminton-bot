CREATE TABLE IF NOT EXISTS game (
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

CREATE INDEX IF NOT EXISTS idx_game_chat_id ON game(chat_id);

CREATE INDEX IF NOT EXISTS idx_game_date ON game(date);
