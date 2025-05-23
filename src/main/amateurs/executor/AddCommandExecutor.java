package amateurs.executor;

import amateurs.database.CommandDataCache;
import amateurs.database.InMemoryCommandDataCache;
import amateurs.model.AddCommandField;
import amateurs.model.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.UUID;

import static amateurs.Command.ADD_COMMAND;
import static amateurs.Command.COMMAND_DELIMITER;
import static amateurs.util.AddCommandFieldProcessor.getKeyboardForField;
import static amateurs.util.AddCommandFieldProcessor.getTextForField;
import static amateurs.util.InputToModelMapper.mapInputToGame;

/**
 * Adds a game by constructing a Game string with this format:
 * /add:{location}:{courts}:{date}:{time period}:{start hour}:{start minute}:{duration}:{pricePerPax}
 *
 * Callback data for keyboard buttons follows this format:
 * /add:{UUID}:{new_field_value}
 */
public class AddCommandExecutor extends BaseCommandExecutor implements CommandExecutor {
    public static final String ADD_TEMPLATE = """            
            Where and which court(s) will the game be at?
            
            Respond in this format:
            <pre>
            /add:{location}:{courts}
            </pre>
            
            For example, if you are playing at Choa Chu Kang Sports Hall and you have courts 2 and 4, reply like this:
            <pre>
            /add:Choa Chu Kang Sports Hall:2,4
            </pre>
            """;

    private final CommandDataCache commandDataCache;

    private static final Logger LOG = LogManager.getLogger();

    public AddCommandExecutor() {
        super();
        this.commandDataCache = InMemoryCommandDataCache.getInstance();
    }

    @Override
    public void executeCommand(Long chatId) {
        LOG.info("Executing add command for chatId={}", chatId);
        chatClient.sendText(chatId, ADD_TEMPLATE);
    }

    @Override
    public void executeCommandWithParams(Long chatId, String params) {
        LOG.info("Executing command with params for chatId={} and params={}", chatId, params);
        validateParams(params);
        final String key = UUID.randomUUID().toString();
        commandDataCache.cacheNewCommandData(key, params);
        final String fieldText = getMessageForCurrentData(params);
        final String callbackPrefix = ADD_COMMAND.getCommand() + COMMAND_DELIMITER + key;
        final InlineKeyboardMarkup fieldKeyboard = getKeyboardForCurrentData(params, callbackPrefix);
        chatClient.sendMenu(chatId, fieldText, fieldKeyboard);
    }

    @Override
    public void executeCallbackQuery(Long chatId, int msgId, String queryId, String callbackData) {
        LOG.info("Executing callback query for chatId={}, msgId={}, queryId={} and callbackData={}",
                chatId, msgId, queryId, callbackData);
        chatClient.closeCallbackQuery(queryId);
        final String[] callbackComponents = callbackData.split(COMMAND_DELIMITER);

        // Initial query from help menu (/add only)
        if (callbackComponents.length != 3) {
            chatClient.editMessageWithMenu(chatId, msgId, ADD_TEMPLATE, null);
            return;
        }

        // For other callback queries, the callback data should contain a key for the command data
        final String key = callbackComponents[1];
        final String newFieldValue = callbackComponents[2];
        final String updatedData = commandDataCache.getCommandDataForKey(key) + COMMAND_DELIMITER + newFieldValue;
        commandDataCache.updateCommandData(key, updatedData);
        final AddCommandField field = getCommandField(updatedData);
        LOG.info("Updated data={}", updatedData);
        if (field.isTerminal()) {
            processTerminalField(chatId, msgId, updatedData);
        } else {
            final String fieldText = getMessageForCurrentData(updatedData);
            final String callbackPrefix = ADD_COMMAND.getCommand() + COMMAND_DELIMITER + key;
            final InlineKeyboardMarkup fieldKeyboard = getKeyboardForCurrentData(updatedData, callbackPrefix);
            chatClient.editMessageWithMenu(chatId, msgId, fieldText, fieldKeyboard);
        }
    }

    private void processTerminalField(Long chatId, int msgId, String data) {
        final Game game = mapInputToGame(data);
        game.setChatId(chatId);
        database.addGame(chatId, game);
        chatClient.editMessageWithMenu(chatId, msgId, game.toString(), null);
    }

    private String getMessageForCurrentData(String data) {
        final AddCommandField currentField = getCommandField(data);
        return getTextForField(currentField, data);
    }

    private InlineKeyboardMarkup getKeyboardForCurrentData(String data, String callbackPrefix) {
        final AddCommandField currentField = getCommandField(data);
        return currentField.isUseKeyboard()
                ? getKeyboardForField(currentField, callbackPrefix)
                : null;
    }

    private AddCommandField getCommandField(String data) {
        int currentStep = data.split(COMMAND_DELIMITER).length;
        return AddCommandField.getFieldByStep(currentStep);
    }

    private void validateParams(String params) {
        if (params.split(COMMAND_DELIMITER).length != 3) {
            throw new IllegalArgumentException("Add command should follow this format: /add:{location}:{courts}");
        }
    }
}
