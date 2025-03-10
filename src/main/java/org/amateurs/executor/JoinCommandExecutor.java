package org.amateurs.executor;

import org.amateurs.ChatClient;
import org.amateurs.database.Database;
import org.amateurs.database.InMemoryDatabase;
import org.amateurs.model.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.Arrays;
import java.util.List;

import static org.amateurs.Command.COMMAND_DELIMITER;
import static org.amateurs.Command.JOIN_COMMAND;
import static org.amateurs.components.OptionsKeyboardBuilder.buildOptionsKeyboard;
import static org.amateurs.util.CommandExecutorUtil.generateSequentialInt;

/**
 * Adds players to a game by constructing a Game string with this format:
 * /join:{gameId}:{players}
 */
public class JoinCommandExecutor implements CommandExecutor {
    public static final String JOIN_TEMPLATE = """
            Which game would you like to join players to?
            """;

    public static final String NO_GAME_FOUND_TEMPLATE = """
            Please add any game first! Type /start to begin.
            """;

    public static final String JOIN_GAME_ID_TEMPLATE = """
            Who are the players joining the game?
            
            Respond in this format:
            <pre>
            /join:%s:{players}
            </pre>
            
            For example, if you want to add PlayerA and PlayerB, reply like this:
            <pre>
            /join:%s:PlayerA,PlayerB
            </pre>
            
            You can copy this template:
            <pre>
            /join:%s:
            </pre>
            """;

    public static final String UPDATED_GAME_TEMPLATE = """
            Here's the updated game:
    
    %s
    """;

    public static final String GAME_NOT_FOUND_TEMPLATE = """
            This game does not exist. Please try again with the /join command.
            """;

    private final ChatClient chatClient;

    private final Database database;

    private static final Logger LOG = LogManager.getLogger();

    public JoinCommandExecutor() {
        this.chatClient = ChatClient.getInstance();
        this.database = InMemoryDatabase.getInstance();
    }

    @Override
    public void executeCommand(Long chatId) {
        LOG.info("Executing join command for chatId={}", chatId);
        final List<String> gameIds = database.getAllGameIds(chatId);
        if (gameIds.isEmpty()) {
            chatClient.sendText(chatId, NO_GAME_FOUND_TEMPLATE);
            return;
        }

        final List<String> labels = generateSequentialInt(1, gameIds.size(), 1)
                .stream()
                .map(gameIdx -> String.format("Game %s", gameIdx))
                .toList();
        final List<String> callbackData = gameIds.stream()
                .map(gameId -> String.join(COMMAND_DELIMITER, JOIN_COMMAND.getCommand(), gameId))
                .toList();
        final InlineKeyboardMarkup gameOptions = buildOptionsKeyboard(labels, callbackData, 3);
        chatClient.sendMenu(chatId, JOIN_TEMPLATE, gameOptions);
    }

    @Override
    public void executeCallbackQuery(Long chatId, int msgId, String queryId, String callbackData) {
        LOG.info("Executing callback query for chatId={}, msgId={}, queryId={} and callbackData={}",
                chatId, msgId, queryId, callbackData);
        chatClient.closeCallbackQuery(queryId);
        final String[] callbackComponents = callbackData.split(COMMAND_DELIMITER);
        if (callbackComponents.length < 2) {
            executeCommand(chatId);
        } else if (callbackComponents.length == 2) {
            sendInstructionsToAddPlayers(chatId, msgId, callbackComponents);
        }
    }

    @Override
    public void executeCommandWithParams(Long chatId, String params) {
        LOG.info("Executing command with params for chatId={} and params={}", chatId, params);
        validateParams(params);

        final String gameId = params.split(COMMAND_DELIMITER)[1];
        final List<String> players = Arrays.stream(params.split(COMMAND_DELIMITER)[2].split(",")).toList();
        final Game updatedGame = database.addPlayersToGame(gameId, players);

        // If game is not found
        if (updatedGame == null) {
            chatClient.sendText(chatId, GAME_NOT_FOUND_TEMPLATE);
            return;
        }

        chatClient.sendText(chatId, String.format(UPDATED_GAME_TEMPLATE, updatedGame));
    }

    private void sendInstructionsToAddPlayers(Long chatId, int msgId, String[] callbackComponents) {
        final String gameId = callbackComponents[1];
        final String instructions = String.format(JOIN_GAME_ID_TEMPLATE, gameId, gameId, gameId);
        chatClient.editMessageWithMenu(chatId, msgId, instructions, null);
    }

    private void validateParams(String params) {
        if (params.split(COMMAND_DELIMITER).length != 3) {
            throw new IllegalArgumentException("Join command should follow this format: /join:{gameId}:{players}");
        }
    }
}
