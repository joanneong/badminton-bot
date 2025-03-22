package org.amateurs.util;

import org.amateurs.model.AddCommandField;
import org.amateurs.model.Game;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static org.amateurs.Command.COMMAND_DELIMITER;
import static org.amateurs.components.DateOptionsKeyboardBuilder.ASIA_TIMEZONE;

public class InputToModelMapper {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd LLL yyyy (EEE)", Locale.US);

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("h:mma", Locale.US);

    public static Game mapInputToGame(String gameId, String input) {
        final String[] components = input.split(COMMAND_DELIMITER);
        final String location = components[AddCommandField.LOCATION.getStep()];
        final List<String> courts = List.of(components[AddCommandField.COURTS.getStep()].split(","));
        final String dateInput = components[AddCommandField.DATE.getStep()];
        final LocalDate date = parseDate(dateInput);
        final LocalTime startTime = parseStartTimeInput(components);
        final String duration = components[AddCommandField.DURATION.getStep()];
        final String maxPlayers = components[AddCommandField.MAX_PLAYERS.getStep()];
        final String pricePerPax = components[AddCommandField.PRICE_PER_PAX.getStep()];

        return Game.builder()
                .id(gameId)
                .location(location)
                .courts(courts)
                .date(date)
                .startTime(startTime)
                .endTime(startTime.plusHours(Long.parseLong(duration)))
                .maxPlayers(Integer.parseInt(maxPlayers))
                .pricePerPax(Integer.parseInt(pricePerPax))
                .build();
    }

    private static LocalDate parseDate(String input) {
        final ZonedDateTime now = ZonedDateTime.now(ASIA_TIMEZONE);
        final String inputWithYear = String.join(" ", input, String.valueOf(now.getYear()));
        final DateTimeFormatter fullDateFormatter = DateTimeFormatter.ofPattern("dd LLL (EEE) u", Locale.US);
        return LocalDate.parse(inputWithYear, fullDateFormatter);
    }

    private static LocalTime parseStartTimeInput(String[] input) {
        final String timePeriod = input[AddCommandField.TIME_PERIOD.getStep()];
        final String startHour = input[AddCommandField.START_HOUR.getStep()];
        final String startMinute = input[AddCommandField.START_MINUTE.getStep()];
        final String fullStartTime = String.join(":", startHour, startMinute, timePeriod);
        final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:m:a", Locale.US);
        return LocalTime.parse(fullStartTime, timeFormatter);
    }
}
