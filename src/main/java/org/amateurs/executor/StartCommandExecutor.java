package org.amateurs.executor;

import org.amateurs.ChatClient;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import static org.amateurs.Command.ADD_COMMAND;
import static org.amateurs.Command.DELETE_COMMAND;
import static org.amateurs.Command.EDIT_COMMAND;
import static org.amateurs.Command.LIST_COMMAND;

public class StartCommandExecutor {
    private static final String BADMINTON_EMOJI = new String(Character.toChars(
            Integer.parseInt("1F3F8", 16)));

    private static final String HELP_TEMPLATE = """
                Welcome to BadmintonBot! %s
                
                What do you want to do today?
                """;

    private final InlineKeyboardMarkup startMenu;

    private final ChatClient chatClient;

    public StartCommandExecutor() {
        var listButton = InlineKeyboardButton.builder()
                .text("List all games").callbackData(LIST_COMMAND.getCommand())
                .build();

        var addButton = InlineKeyboardButton.builder()
                .text("Add a game").callbackData(ADD_COMMAND.getCommand())
                .build();

        var editButton = InlineKeyboardButton.builder()
                .text("Edit a game").callbackData(EDIT_COMMAND.getCommand())
                .build();

        var deleteButton = InlineKeyboardButton.builder()
                .text("Delete a game").callbackData(DELETE_COMMAND.getCommand())
                .build();

        this.chatClient = ChatClient.getInstance();

        this.startMenu = InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(listButton))
                .keyboardRow(new InlineKeyboardRow(addButton))
                .keyboardRow(new InlineKeyboardRow(editButton))
                .keyboardRow(new InlineKeyboardRow(deleteButton))
                .build();
    }

    public void executeStartCommand(Long chatId) {
        final String helpMessage = String.format(HELP_TEMPLATE, BADMINTON_EMOJI);
        chatClient.sendMenu(chatId, helpMessage, startMenu);
    }
}
