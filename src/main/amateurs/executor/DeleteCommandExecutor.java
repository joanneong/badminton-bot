package amateurs.executor;

import amateurs.ChatClient;
import amateurs.database.Database;
import amateurs.database.InMemoryDatabase;
import amateurs.model.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

import static amateurs.Command.COMMAND_DELIMITER;
import static amateurs.Command.DELETE_COMMAND;
import static amateurs.executor.ListCommandExecutor.NO_GAME_TEMPLATE;
import static amateurs.util.CommandExecutorUtil.generateKeyboardForGames;
import static amateurs.util.CommandExecutorUtil.generateMessageWithGameInfo;

/**
 * Supported formats:
 * 1. /delete - get keyboard to indicate which game to delete
 * 2. /delete:{gameId} - delete game
 */
public class DeleteCommandExecutor implements CommandExecutor{
    private static final String DELETE_TEMPLATE = """
            Which game do you want to delete?
            """;

    private static final String DELETE_SUCCESS = """
            Your game has been deleted successfully! You can use /list to double-check.
            """;

    private static final String DELETE_FAILURE = """
            Your game was not deleted as it does not exist! Please try again with /delete.
            """;

    private final ChatClient chatClient;

    private final Database database;

    private static final Logger LOG = LogManager.getLogger();

    public DeleteCommandExecutor() {
        this.chatClient = ChatClient.getInstance();
        this.database = InMemoryDatabase.getInstance();
    }

    @Override
    public void executeCommand(Long chatId) {
        LOG.info("Executing delete command for chatId={}", chatId);
        final List<Game> allGames = database.getAllGames(chatId);
        if (allGames.isEmpty()) {
            chatClient.sendText(chatId, NO_GAME_TEMPLATE);
            return;
        }

        final String invitation = generateMessageWithGameInfo(DELETE_TEMPLATE, allGames);
        final List<Long> gameIds = allGames.stream().map(Game::getId).toList();
        final InlineKeyboardMarkup keyboard = generateKeyboardForGames(DELETE_COMMAND, gameIds);
        chatClient.sendMenu(chatId, invitation, keyboard);
    }

    @Override
    public void executeCallbackQuery(Long chatId, int msgId, String queryId, String callbackData) {
        LOG.info("Executing callback query for chatId={}, msgId={}, queryId={} and callbackData={}",
                chatId, msgId, queryId, callbackData);
        chatClient.closeCallbackQuery(queryId);

        final String[] callbackComponents = callbackData.split(COMMAND_DELIMITER);

        if (callbackComponents.length == 1) {
            executeCommand(chatId);
        } else if (callbackComponents.length == 2) {
            boolean isGameDeleted = database.deleteGame(chatId, Long.valueOf(callbackComponents[1]));
            final String returnMsg = isGameDeleted ? DELETE_SUCCESS : DELETE_FAILURE;
            chatClient.sendText(chatId, returnMsg);
        }
    }
}
