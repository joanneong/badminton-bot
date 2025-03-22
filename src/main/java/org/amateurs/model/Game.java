package org.amateurs.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.amateurs.util.InputToModelMapper.DATE_FORMATTER;
import static org.amateurs.util.InputToModelMapper.TIME_FORMATTER;
import static org.amateurs.util.StringDisplayUtil.getBulletedList;
import static org.amateurs.util.StringDisplayUtil.getFormattedList;

@Data
@Builder
public class Game implements Comparable<Game> {
    private static final String GAME_ID_TEMPLATE = """
            <u><b>Game %d</b></u>
            """;

    private static final String GAME_INFO_TEMPLATE = """
            <b>Date:</b> %s
            <b>Time:</b> %s to %s
            <b>Location:</b> %s
            <b>Court(s):</b> %s
            """;

    private static final String FULL_GAME_INFO_TEMPLATE = """
            <b>Date:</b> %s
            <b>Time:</b> %s to %s
            <b>Location:</b> %s
            <b>Court(s):</b> %s
            <b>Players(s):</b> %s
            """;

    @NonNull
    String id;

    @NonNull
    LocalDate date;

    @NonNull
    LocalTime startTime;

    @NonNull
    LocalTime endTime;

    @NonNull
    String location;

    @NonNull
    List<String> courts;

    @Builder.Default
    List<Player> players = new ArrayList<>();

    int maxPlayers;

    int pricePerPax;

    @Override
    public int compareTo(@NotNull Game otherGame) {
        return this.date.isEqual(otherGame.getDate())
                ? this.startTime.compareTo(otherGame.getStartTime())
                : this.date.compareTo(otherGame.getDate());
    }

    @Override
    public String toString() {
        return String.format(GAME_INFO_TEMPLATE,
                DATE_FORMATTER.format(date),
                TIME_FORMATTER.format(startTime).toUpperCase(),
                TIME_FORMATTER.format(endTime).toUpperCase(),
                location,
                getFormattedList(courts));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!(o instanceof Game otherGame)) {
            return false;
        }

        return this.id.equals(otherGame.getId());
    }

    /**
     * Get the game details in a formatted string
     */
    public String getGameInfoString(int idx) {
        return String.format(GAME_ID_TEMPLATE, idx) + this;
    }

    public String getFullGameInfoString(int idx) {
        final String additionalGameInfo = String.format(FULL_GAME_INFO_TEMPLATE,
                DATE_FORMATTER.format(date),
                TIME_FORMATTER.format(startTime).toUpperCase(),
                TIME_FORMATTER.format(endTime).toUpperCase(),
                location,
                getFormattedList(courts),
                getBulletedList(players.stream().map(Player::getName).toList(), maxPlayers));
        return String.format(GAME_ID_TEMPLATE, idx) + additionalGameInfo;
    }
}
