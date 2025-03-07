package org.amateurs.model;

import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Data
@Builder
public class Game implements Comparable<Game> {
    private static final String GAME_ID_TEMPLATE = """
            <u><b>Game %d</b></u>
            """;

    private static final String DISPLAY_TEMPLATE = """
            <b>Date:</b> %s
            <b>Time:</b> %s to %s
            <b>Location:</b> %s
            <b>Court(s):</b> %s
            <b>Players(s):</b> %s
            """;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd LLL yyyy (EEE)", Locale.US);

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("h:mma", Locale.US);

    LocalDate date;

    LocalTime startTime;

    LocalTime endTime;

    String location;

    List<String> courts;

    List<String> players;

    int maxPlayers;

    @Override
    public int compareTo(@NotNull Game otherGame) {
        return this.date.isEqual(otherGame.getDate())
                ? this.startTime.compareTo(otherGame.getStartTime())
                : this.date.compareTo(otherGame.getDate());
    }

    @Override
    public String toString() {
        return String.format(DISPLAY_TEMPLATE,
                DATE_FORMATTER.format(date),
                TIME_FORMATTER.format(startTime).toUpperCase(),
                TIME_FORMATTER.format(endTime).toUpperCase(),
                location,
                getFormattedList(courts),
                getFormattedList(players));
    }

    /**
     * Get the game details in a formatted string
     */
    public String toDisplayString(int idx) {
        return String.format(GAME_ID_TEMPLATE, idx) + this;
    }

    private String getFormattedList(List<String> toFormat) {
        if (toFormat == null || toFormat.isEmpty()) {
            return "";
        }
        return String.join(", ", toFormat);
    }
}
