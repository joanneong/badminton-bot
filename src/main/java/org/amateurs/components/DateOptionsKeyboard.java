package org.amateurs.components;

import org.amateurs.Command;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DateOptionsKeyboard {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd LLL yyyy (EEE)");

    public static InlineKeyboardMarkup buildDateOptionsKeyboard(int numDays, int buttonsPerRow, String callbackPrefix) {
        final ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Kuala_Lumpur"));

        int numRows = Math.ceilDiv(numDays, buttonsPerRow);

        final List<List<InlineKeyboardButton>> dateButtons = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            dateButtons.add(new ArrayList<>());
        }

        for (int i = 0; i < numDays; i++) {
            final List<InlineKeyboardButton> dateRow = dateButtons.get(i / buttonsPerRow);
            final InlineKeyboardButton dateButton = buildDateButton(now.plusDays(i + 1), callbackPrefix);
            dateRow.add(dateButton);
        }

        final List<InlineKeyboardRow> dateRows = dateButtons.stream()
                .map(InlineKeyboardRow::new)
                .toList();

        return InlineKeyboardMarkup.builder()
                .keyboard(dateRows)
                .build();
    }

    /**
     * Returns a date button corresponding to an input date
     * @param date              input date to create button for
     * @param callbackPrefix    prefix to use for button callback data
     * @return                  date button corresponding to input date
     */
    public static InlineKeyboardButton buildDateButton(ZonedDateTime date, String callbackPrefix) {
        final String formattedDate = DATE_FORMATTER.format(date);
        return InlineKeyboardButton.builder()
                .text(formattedDate)
                .callbackData(String.join(Command.COMMAND_DELIMITER, callbackPrefix, formattedDate))
                .build();
    }
}
