package org.amateurs.util;

import org.amateurs.model.AddCommandField;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import static org.amateurs.Command.COMMAND_DELIMITER;
import static org.amateurs.components.DateOptionsKeyboardBuilder.buildDateOptionsKeyboard;
import static org.amateurs.executor.AddCommandExecutor.ADD_TEMPLATE;

public class AddCommandFieldProcessor {
    public static String getTextForField(AddCommandField field, String callbackData) {
        final String formattedCallbackData = formatExistingData(callbackData);
        return formattedCallbackData + switch (field) {
            case LOCATION, COURTS -> ADD_TEMPLATE;
            case DATE -> "Which date is the game on?";
            case TIME_PERIOD -> "Is the game in the AM or PM?";
            case START_HOUR -> "What hour does the game start at?";
            case START_MINUTE -> "What minute does the game start at?";
            case DURATION -> "How long will the game last for?";
            case MAX_PLAYERS -> "What's the max number of players for the game?";
        };
    }

    public static InlineKeyboardMarkup getKeyboardForField(AddCommandField field, String data) {
        return buildDateOptionsKeyboard(15, 3, data);
    }

    private static String formatExistingData(String data) {
        String[] params = data.split(COMMAND_DELIMITER);
        StringBuilder formattedData = new StringBuilder();
        for (int i = 1; i < params.length; i++) {
            final AddCommandField field = AddCommandField.getFieldByStep(i);
            formattedData.append(field.getDisplayName()).append(": ").append(params[i]).append("\n");
        }
        formattedData.append("\n");
        return formattedData.toString();
    }
}
