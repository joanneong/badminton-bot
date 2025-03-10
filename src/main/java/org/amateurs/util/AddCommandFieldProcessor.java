package org.amateurs.util;

import org.amateurs.model.AddCommandField;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

import static org.amateurs.Command.COMMAND_DELIMITER;
import static org.amateurs.components.DateOptionsKeyboardBuilder.buildDateOptionsKeyboard;
import static org.amateurs.components.OptionsKeyboardBuilder.buildOptionsKeyboard;
import static org.amateurs.executor.AddCommandExecutor.ADD_TEMPLATE;
import static org.amateurs.util.CommandExecutorUtil.generateSequentialInt;

public class AddCommandFieldProcessor {
    public static String getTextForField(AddCommandField field, String callbackData) {
        final String formattedCallbackData = formatExistingData(callbackData);
        return formattedCallbackData + switch (field) {
            case LOCATION, COURTS -> ADD_TEMPLATE;
            case DATE -> "Which date is the game on?";
            case TIME_PERIOD -> "Is the game in the AM or PM?";
            case START_HOUR -> "What hour does the game start at?";
            case START_MINUTE -> "What minute does the game start at?";
            case DURATION -> "How many hours will the game last for?";
            case MAX_PLAYERS -> "What's the max number of players for the game?";
            case CONFIRM_GAME -> null;
        };
    }

    public static InlineKeyboardMarkup getKeyboardForField(AddCommandField field, String data) {
        return switch (field) {
            case LOCATION, COURTS, CONFIRM_GAME -> null;
            case DATE -> buildDateOptionsKeyboard(15, 3, data);
            case TIME_PERIOD -> buildOptionsKeyboard(List.of("AM", "PM"), data,2);
            case START_HOUR -> buildOptionsKeyboard(generateSequentialInt(1, 12, 1), data, 4);
            case START_MINUTE -> buildOptionsKeyboard(generateSequentialInt(0, 45, 15), data, 4);
            case DURATION -> buildOptionsKeyboard(generateSequentialInt(1, 4, 1), data, 4);
            case MAX_PLAYERS -> buildOptionsKeyboard(generateSequentialInt(1, 13, 1), data, 4);
        };
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
