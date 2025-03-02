package org.amateurs.executor;

import org.amateurs.ChatClient;
import org.amateurs.database.Database;
import org.amateurs.database.InMemoryDatabase;
import org.amateurs.model.AddCommandField;
import org.amateurs.model.Game;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import static org.amateurs.Command.COMMAND_DELIMITER;
import static org.amateurs.util.AddCommandFieldProcessor.getKeyboardForField;
import static org.amateurs.util.AddCommandFieldProcessor.getTextForField;
import static org.amateurs.util.InputToModelMapper.mapInputToGame;

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
            
            For example, if you are playing at Choa Chu Kang Sports Hall and you have courts 2 and 4, reply like this:
            <pre>
            /add::Choa Chu Kang Sports Hall::2,4
            </pre>
            """;

    private final ChatClient chatClient;

    private final Database database;

    public AddCommandExecutor() {
        this.chatClient = ChatClient.getInstance();
        this.database = InMemoryDatabase.getInstance();
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
        final AddCommandField field = getCommandField(callbackData);
        if (field.isTerminal()) {
            processTerminalField(chatId, msgId, callbackData);
        } else {
            final String fieldText = getMessageForCurrentData(callbackData);
            final InlineKeyboardMarkup fieldKeyboard = getKeyboardForCurrentData(callbackData);
            chatClient.editMessageWithMenu(chatId, msgId, fieldText, fieldKeyboard);
        }
    }

    private void processTerminalField(Long chatId, int msgId, String data) {
        final Game game = mapInputToGame(data);
        database.addGame(chatId, game);
        chatClient.editMessageWithMenu(chatId, msgId, game.toString(), null);
    }

    private String getMessageForCurrentData(String data) {
        final AddCommandField currentField = getCommandField(data);
        return getTextForField(currentField, data);
    }

    private InlineKeyboardMarkup getKeyboardForCurrentData(String data) {
        final AddCommandField currentField = getCommandField(data);
        return currentField.isUseKeyboard()
                ? getKeyboardForField(currentField, data)
                : null;
    }

    private AddCommandField getCommandField(String data) {
        int currentStep = data.split(COMMAND_DELIMITER).length;
        return AddCommandField.getFieldByStep(currentStep);
    }
}
