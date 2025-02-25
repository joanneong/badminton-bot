package org.amateurs.executor;

import org.amateurs.ChatClient;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import static org.amateurs.Command.ADD_COMMAND;
import static org.amateurs.components.DateOptionsKeyboardBuilder.buildDateOptionsKeyboard;

public class AddCommandExecutor implements CommandExecutor {
    private static final String ADD_TEMPLATE = """
            Please provide your game details :)
            
            When is the game going to be?
            """;

    private final ChatClient chatClient;

    public AddCommandExecutor() {
        this.chatClient = ChatClient.getInstance();
    }

    @Override
    public void executeCommand(Long chatId) {
        final InlineKeyboardMarkup dateKeyboard = buildDateOptionsKeyboard(15, 5, ADD_COMMAND.getCommand());
        chatClient.sendMenu(chatId, ADD_TEMPLATE, dateKeyboard);
    }

    @Override
    public void executeCallbackQuery(Long chatId, int msgId, String queryId, String callbackData) {
        chatClient.closeCallbackQuery(queryId);
        executeCommand(chatId);
    }
}
