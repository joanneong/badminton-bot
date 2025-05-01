package amateurs.executor;

import amateurs.model.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.Arrays;
import java.util.List;

import static amateurs.Command.COMMAND_DELIMITER;
import static amateurs.Command.JOIN_COMMAND;
import static amateurs.util.CommandExecutorUtil.generateKeyboardForGames;
import static amateurs.util.CommandExecutorUtil.generateMessageWithGameInfo;

/**
 * Adds players to a game by constructing a Game string with this format:
 * /join:{gameId}:{players}
 */
public class JoinCommandExecutor extends BaseCommandExecutor implements CommandExecutor {
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

    private static final Logger LOG = LogManager.getLogger();

    public JoinCommandExecutor() {
        super();
    }

    @Override
    public void executeCommand(Long chatId) {
        LOG.info("Executing join command for chatId={}", chatId);
        final List<Game> allGames = database.getAllGames(chatId);
        if (allGames.isEmpty()) {
            chatClient.sendText(chatId, NO_GAME_FOUND_TEMPLATE);
            return;
        }

        final String joinMsg = generateMessageWithGameInfo(JOIN_TEMPLATE, allGames);
        final List<Long> gameIds = allGames.stream().map(Game::getId).toList();
        final InlineKeyboardMarkup keyboard = generateKeyboardForGames(JOIN_COMMAND, gameIds);
        chatClient.sendMenu(chatId, joinMsg, keyboard);
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

        final Long gameId = Long.valueOf(params.split(COMMAND_DELIMITER)[1]);
        final List<String> players = Arrays.stream(params.split(COMMAND_DELIMITER)[2].split(",")).toList();
        final Game updatedGame = database.addPlayersToGame(chatId, gameId, players);

        // If game is not found
        if (updatedGame == null) {
            chatClient.sendText(chatId, GAME_NOT_FOUND_TEMPLATE);
            return;
        }

        chatClient.sendText(chatId, String.format(UPDATED_GAME_TEMPLATE, updatedGame.getFullGameInfoString()));
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
