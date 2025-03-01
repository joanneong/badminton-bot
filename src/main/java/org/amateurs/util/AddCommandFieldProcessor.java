package org.amateurs.util;

import org.amateurs.model.AddCommandField;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import static org.amateurs.Command.ADD_COMMAND;
import static org.amateurs.components.DateOptionsKeyboardBuilder.buildDateOptionsKeyboard;
import static org.amateurs.executor.AddCommandExecutor.ADD_TEMPLATE;

public class AddCommandFieldProcessor {
    public static String getTextForField(AddCommandField field, String callbackData) {
        final String formattedCallbackData = formatExistingData(callbackData);
        return switch (field) {
            case LOCATION, COURTS -> ADD_TEMPLATE;
            case DATE -> "Which date";
            case TIME_PERIOD -> "Which time period";
            case START_HOUR -> "Which start hour";
            case START_MINUTE -> "Which start minute";
            case DURATION -> "What duration";
        };
    }

    public static InlineKeyboardMarkup getKeyboardForField(AddCommandField field) {
        return buildDateOptionsKeyboard(15, 5, ADD_COMMAND.getCommand());
    }

    private static String formatExistingData(String data) {
        return "ABC";
    }
}
