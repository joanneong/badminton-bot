package org.amateurs.executor;

import org.amateurs.ChatClient;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import static org.amateurs.Command.*;

public class StartCommandExecutor {
    private static final String BADMINTON_EMOJI = new String(Character.toChars(
            Integer.parseInt("1F3F8", 16)));

    private static final String helpTemplate = """
                Welcome to BadmintonBot! %s
                
                What do you want to do today?
                """;

    private final InlineKeyboardMarkup startMenu;

    private final ChatClient chatClient;

    public StartCommandExecutor() {
        var list_button = InlineKeyboardButton.builder()
                .text("List all games").callbackData(LIST_COMMAND.getCommand())
                .text("Add a game").callbackData(ADD_COMMAND.getCommand())
                .text("Edit a game").callbackData(EDIT_COMMAND.getCommand())
                .text("Delete a game").callbackData(DELETE_COMMAND.getCommand())
                .build();

        this.chatClient = ChatClient.getInstance();

        this.startMenu = InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(list_button))
                .build();
    }

    public void executeStartCommand(Long chatId) {
        final String helpMessage = String.format(helpTemplate, BADMINTON_EMOJI);
        chatClient.sendMenu(chatId, helpMessage, startMenu);
    }
}
