package org.amateurs.components;

import org.amateurs.Command;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Builds an inline keyboard containing buttons for specified options and callback prefix
     * @param options           button options (will be part of callback data)
     * @param callbackPrefix    callback prefix for each button
     * @param buttonsPerRow     number of buttons per keyboard row
     * @return                  an inline keyboard
     */
    public static InlineKeyboardMarkup buildOptionsKeyboard(List<String> options, String callbackPrefix,
                                                            int buttonsPerRow) {
        final List<InlineKeyboardButton> buttons = options.stream()
                .map(option -> InlineKeyboardButton.builder()
                        .text(option)
                        .callbackData(String.join(Command.COMMAND_DELIMITER, callbackPrefix, option))
                        .build())
                .collect(Collectors.toUnmodifiableList());
        return buildOptionsKeyboard(buttons, buttonsPerRow);
    }

    /**
     * Builds an inline keyboard containing buttons with specified labels and callback prefix
     * @param labels            label for each button (ordered)
     * @param callbacks         callback for each button (ordered)
     * @param buttonsPerRow     number of buttons per keyboard row
     * @return                  an inline keyboard
     */
    public static InlineKeyboardMarkup buildOptionsKeyboard(List<String> labels, List<String> callbacks,
                                                            int buttonsPerRow) {
        assert labels != null && callbacks != null;
        if (labels.size() != callbacks.size()) {
            throw new RuntimeException("Number of labels should match number of callback prefixes!");
        }

        final List<InlineKeyboardButton> buttons = new ArrayList<>();
        final Iterator<String> labelIterator = labels.iterator();
        final Iterator<String> callbackIterator = callbacks.iterator();
        while (labelIterator.hasNext() && callbackIterator.hasNext()) {
            buttons.add(InlineKeyboardButton.builder()
                    .text(labelIterator.next())
                    .callbackData(callbackIterator.next())
                    .build());
        }
        return buildOptionsKeyboard(buttons, buttonsPerRow);
    }
}
