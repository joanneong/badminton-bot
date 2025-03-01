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
            <code>
            /add::{location}::{courts}
            </code>
            
            For example, if you are playing at Choa Chu Kang Sports Hall and you have courts 2 and 4, reply like this:
            <code>
            /add::Choa Chu Kang Sports Hall::2,4
            </code>
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
        final String fieldText = getMessageForCurrentData(params);
        final InlineKeyboardMarkup fieldKeyboard = getKeyboardForCurrentData(params);
        chatClient.sendMenu(chatId, fieldText, fieldKeyboard);
    }

    @Override
    public void executeCallbackQuery(Long chatId, int msgId, String queryId, String callbackData) {
        chatClient.closeCallbackQuery(queryId);
        final String fieldText = getMessageForCurrentData(callbackData);
        final InlineKeyboardMarkup fieldKeyboard = getKeyboardForCurrentData(callbackData);
        chatClient.editMessageWithMenu(chatId, msgId, fieldText, fieldKeyboard);
    }

    private String getMessageForCurrentData(String data) {
        int currentStep = data.split(COMMAND_DELIMITER).length;
        final AddCommandField currentField = AddCommandField.getFieldByStep(currentStep);
        return getTextForField(currentField, data);
    }

    private InlineKeyboardMarkup getKeyboardForCurrentData(String data) {
        int currentStep = data.split(COMMAND_DELIMITER).length;
        final AddCommandField currentField = AddCommandField.getFieldByStep(currentStep);
        return currentField.isUseKeyboard()
                ? getKeyboardForField(currentField, data)
                : null;
    }
}
