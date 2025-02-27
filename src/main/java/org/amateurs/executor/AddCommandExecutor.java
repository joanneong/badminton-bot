package org.amateurs.executor;

import org.amateurs.ChatClient;
import org.amateurs.model.AddCommandField;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import static org.amateurs.Command.ADD_COMMAND;
import static org.amateurs.Command.COMMAND_DELIMITER;
import static org.amateurs.components.DateOptionsKeyboardBuilder.buildDateOptionsKeyboard;
import static org.amateurs.util.AddCommandFieldProcessor.getKeyboardForField;
import static org.amateurs.util.AddCommandFieldProcessor.getTextForField;

/**
 * Adds a game by constructing a Game string with this format:
 * /add::{date}::{time period}::{start hour}::{start minute}::{duration}::{location}::{courts}
 */
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

        int currentStep = callbackData.split(COMMAND_DELIMITER).length;
        final AddCommandField currentField = AddCommandField.getFieldByStep(currentStep);
        final String fieldText = getTextForField(currentField, callbackData);
        final InlineKeyboardMarkup fieldKeyboard = getKeyboardForField(currentField);
        chatClient.editMessageWithMenu(chatId, msgId, fieldText, fieldKeyboard);
    }
}
