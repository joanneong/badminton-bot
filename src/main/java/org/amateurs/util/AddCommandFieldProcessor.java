package org.amateurs.util;

import org.amateurs.model.AddCommandField;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import static org.amateurs.Command.ADD_COMMAND;
import static org.amateurs.components.DateOptionsKeyboardBuilder.buildDateOptionsKeyboard;

public class AddCommandFieldProcessor {
    public static String getTextForField(AddCommandField field, String callbackData) {
        return "double whammy";
    }

    public static InlineKeyboardMarkup getKeyboardForField(AddCommandField field) {
        return buildDateOptionsKeyboard(15, 5, ADD_COMMAND.getCommand());
    }
}
