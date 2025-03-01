package org.amateurs.executor;

import org.amateurs.ChatClient;
import org.amateurs.model.AddCommandField;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import static org.amateurs.Command.COMMAND_DELIMITER;
import static org.amateurs.util.AddCommandFieldProcessor.getKeyboardForField;
import static org.amateurs.util.AddCommandFieldProcessor.getTextForField;

/**
 * Adds a game by constructing a Game string with this format:
 * /add::{location}::{courts}::{date}::{time period}::{start hour}::{start minute}::{duration}
 */
public class AddCommandExecutor implements CommandExecutor {
    public static final String ADD_TEMPLATE = """            
            Where and which court(s) will the game be at?
            
            Respond in this format:
            <pre>
            /add::{location}::{courts}
            </pre>
            
            For example, if you are playing at Choa Chu Kang Sports Hall and you have courts 2 and 4, reply this:
            <pre>
            /add::Choa Chu Kang Sports Hall::2,4
            </pre>
            """;

    private final ChatClient chatClient;

    public AddCommandExecutor() {
        this.chatClient = ChatClient.getInstance();
    }

    @Override
    public void executeCommand(Long chatId) {
        chatClient.sendText(chatId, ADD_TEMPLATE);
    }

    @Override
    public void executeCommandWithParams(Long chatId, String params) {
        int currentStep = params.split(COMMAND_DELIMITER).length;
        final AddCommandField currentField = AddCommandField.getFieldByStep(currentStep);
        final String fieldText = getTextForField(currentField, params);
        final InlineKeyboardMarkup fieldKeyboard = getKeyboardForField(currentField);
        chatClient.sendMenu(chatId, fieldText, fieldKeyboard);
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
