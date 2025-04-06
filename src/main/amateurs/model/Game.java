package amateurs.model;

import amateurs.mapper.GameCourtDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static amateurs.util.InputToModelMapper.DATE_FORMATTER;
import static amateurs.util.InputToModelMapper.TIME_FORMATTER;
import static amateurs.util.StringDisplayUtil.getBulletedList;
import static amateurs.util.StringDisplayUtil.getFormattedList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
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

    Long id;

    @NonNull
    LocalDate date;

    @JsonProperty("start_time")
    @NonNull
    LocalTime startTime;

    @JsonProperty("end_time")
    @NonNull
    LocalTime endTime;

    @NonNull
    String location;

    @JsonProperty("court")
    @JsonDeserialize(using= GameCourtDeserializer.class)
    @NonNull
    List<String> courts;

    @JsonProperty("player")
    @Builder.Default
    List<Player> players = new ArrayList<>();

    @JsonProperty("max_players")
    int maxPlayers;

    @JsonProperty("price_per_pax")
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

    public String getIndexedGameInfoString(int idx) {
        return String.format(GAME_ID_TEMPLATE, idx) + this;
    }

    public String getIndexedFullGameInfoString(int idx) {
        return String.format(GAME_ID_TEMPLATE, idx) + getFullGameInfoString();
    }

    public String getFullGameInfoString() {
        return String.format(FULL_GAME_INFO_TEMPLATE,
                DATE_FORMATTER.format(date),
                TIME_FORMATTER.format(startTime).toUpperCase(),
                TIME_FORMATTER.format(endTime).toUpperCase(),
                location,
                getFormattedList(courts),
                getBulletedList(players.stream().map(Player::getName).toList(), maxPlayers));
    }
}
