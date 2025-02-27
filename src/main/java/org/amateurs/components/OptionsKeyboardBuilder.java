package org.amateurs.components;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class OptionsKeyboardBuilder {
    public static InlineKeyboardMarkup buildOptionsKeyboard(List<InlineKeyboardButton> allButtons, int buttonsPerRow) {
        int numRows = Math.ceilDiv(allButtons.size(), buttonsPerRow);

        final List<List<InlineKeyboardButton>> groupedButtons = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            groupedButtons.add(new ArrayList<>());
        }

        for (int i = 0; i < allButtons.size(); i++) {
            final List<InlineKeyboardButton> buttonGroup = groupedButtons.get(i / buttonsPerRow);
            buttonGroup.add(allButtons.get(i));
        }

        final List<InlineKeyboardRow> keyboardRows = groupedButtons.stream()
                .map(InlineKeyboardRow::new)
                .toList();

        return InlineKeyboardMarkup.builder()
                .keyboard(keyboardRows)
                .build();
    }
}
