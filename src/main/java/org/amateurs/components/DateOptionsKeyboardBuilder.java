package org.amateurs.components;

import org.amateurs.Command;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DateOptionsKeyboardBuilder {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd LLL (EEE)");

    public static InlineKeyboardMarkup buildDateOptionsKeyboard(int numDays, int buttonsPerRow, String callbackPrefix) {
        final ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Kuala_Lumpur"));
        final List<InlineKeyboardButton> dateButtons = new ArrayList<>();
        for (int i = 0; i < numDays; i++) {
            final InlineKeyboardButton dateButton = buildDateButton(now.plusDays(i + 1), callbackPrefix);
            dateButtons.add(dateButton);
        }
        return OptionsKeyboardBuilder.buildOptionsKeyboard(dateButtons, buttonsPerRow);
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
