package org.amateurs.model;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class Game implements Comparable<Game> {
    private final static String DISPLAY_TEMPLATE = """
            Date: %s
            Game time: %s to %s
            Location: %s
            Court(s): %s
            Players(s): %s
            """;

    ZonedDateTime date;

    ZonedDateTime startTime;

    ZonedDateTime endTime;

    String location;

    List<String> courts;

    List<String> players;

    @Override
    public int compareTo(@NotNull Game otherGame) {
        return this.date.isEqual(otherGame.getDate())
                ? this.startTime.compareTo(otherGame.getDate())
                : this.date.compareTo(otherGame.getDate());
    }

    /**
     * Get the game details in a formatted string
     */
    public String toDisplayString() {
        return String.format(DISPLAY_TEMPLATE, date.toString(), startTime.toString(), endTime.toString(),
                location, courts, players);
    }
}
