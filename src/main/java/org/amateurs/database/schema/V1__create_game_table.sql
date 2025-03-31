CREATE TABLE IF NOT EXISTS game (
    id              BIGSERIAL PRIMARY KEY,
    date            DATE NOT NULL,
    start_time      TIME NOT NULL,
    end_time        TIME NOT NULL,
    location        VARCHAR(128),
    max_players     INTEGER NOT NULL,
    price_per_pax   INTEGER NOT NULL,
    created_on      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
