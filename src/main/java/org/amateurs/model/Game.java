package org.amateurs.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static org.amateurs.util.CommandExecutorUtil.BADMINTON_EMOJI;

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

    private static final String INVITATION_TEMPLATE = """
            Looking for %d more players for friendly doubles! %s
            
            <b>Date:</b> %s
            <b>Time:</b> %s to %s
            <b>Location:</b> %s
            <b>Court(s):</b> %s
            <b>Price/pax:</b> $%d
            <b>Max players:</b> %d
            """;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd LLL yyyy (EEE)", Locale.US);

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("h:mma", Locale.US);

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
    List<String> players = new ArrayList<>();

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
        return String.format(DISPLAY_TEMPLATE,
                DATE_FORMATTER.format(date),
                TIME_FORMATTER.format(startTime).toUpperCase(),
                TIME_FORMATTER.format(endTime).toUpperCase(),
                location,
                getFormattedList(courts),
                getBulletedList(players, maxPlayers));
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
    public String toDisplayString(int idx) {
        return String.format(GAME_ID_TEMPLATE, idx) + this;
    }

    public String toInvitationString() {
        return String.format(INVITATION_TEMPLATE,
                maxPlayers - players.size(),
                BADMINTON_EMOJI,
                DATE_FORMATTER.format(date),
                TIME_FORMATTER.format(startTime).toUpperCase(),
                TIME_FORMATTER.format(endTime).toUpperCase(),
                location,
                getFormattedList(courts),
                pricePerPax,
                maxPlayers);
    }

    public void addPlayers(List<String> newPlayers) {
        final Iterator<String> newPlayerIterator = newPlayers.iterator();
        while (players.size() < maxPlayers && newPlayerIterator.hasNext()) {
            players.add(newPlayerIterator.next());
        }
    }

    private String getFormattedList(List<String> toFormat) {
        if (toFormat == null || toFormat.isEmpty()) {
            return "";
        }
        return String.join(", ", toFormat);
    }

    private String getBulletedList(List<String> items, int maxItems) {
        if (items == null) {
            return "";
        }

        final StringBuilder sb = new StringBuilder("\n");
        for (int i = 1; i <= items.size(); i++) {
            sb.append(i).append(". ").append(items.get(i - 1)).append("\n");
        }

        for (int i = items.size() + 1; i <= maxItems; i++) {
            sb.append(i).append(".").append("\n");
        }

        return sb.toString();
    }
}
