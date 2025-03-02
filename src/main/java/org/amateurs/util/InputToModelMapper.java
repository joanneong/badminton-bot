package org.amateurs.util;

import org.amateurs.model.Game;

import java.time.ZonedDateTime;
import java.util.List;

public class InputToModelMapper {
    public static Game mapInputToGame(String input) {
        final ZonedDateTime now = ZonedDateTime.now();
        return Game.builder()
                .date(now.plusDays(3))
                .startTime(now.plusDays(3).plusHours(1))
                .endTime(now.plusDays(3).plusHours(3))
                .location("CCK Sports Hall")
                .courts(List.of("1"))
                .players(List.of("Jojopup123"))
                .build();
    }
}
