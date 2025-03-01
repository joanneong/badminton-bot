package org.amateurs.model;

import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@Builder
public class Game implements Comparable<Game> {
    private static final String DISPLAY_TEMPLATE = """
            <u><b>Game %d</b></u>
            <b>Date:</b> %s
            <b>Time:</b> %s to %s
            <b>Location:</b> %s
            <b>Court(s):</b> %s
            <b>Players(s):</b> %s
            """;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd LLL yyyy (EEE)");

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("h:mma");

    ZonedDateTime date;

    ZonedDateTime startTime;

    ZonedDateTime endTime;

    String location;

    List<String> courts;

    List<String> players;

    int maxPlayers;

    @Override
    public int compareTo(@NotNull Game otherGame) {
        return this.date.isEqual(otherGame.getDate())
                ? this.startTime.compareTo(otherGame.getDate())
                : this.date.compareTo(otherGame.getDate());
    }

    /**
     * Get the game details in a formatted string
     */
    public String toDisplayString(int idx) {
        return String.format(DISPLAY_TEMPLATE,
                idx,
                DATE_FORMATTER.format(date),
                TIME_FORMATTER.format(startTime).toUpperCase(),
                TIME_FORMATTER.format(endTime).toUpperCase(),
                location,
                getFormattedList(courts),
                getFormattedList(players));
    }

    private String getFormattedList(List<String> toFormat) {
        return String.join(", ", toFormat);
    }
}
