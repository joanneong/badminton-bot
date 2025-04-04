package amateurs.executor;

import amateurs.ChatClient;
import amateurs.database.Database;
import amateurs.database.InMemoryDatabase;
import amateurs.model.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;
import java.util.Optional;

import static amateurs.Command.COMMAND_DELIMITER;
import static amateurs.Command.INVITE_COMMAND;
import static amateurs.executor.ListCommandExecutor.NO_GAME_TEMPLATE;
import static amateurs.util.CommandExecutorUtil.BADMINTON_EMOJI;
import static amateurs.util.CommandExecutorUtil.generateKeyboardForGames;
import static amateurs.util.CommandExecutorUtil.generateMessageWithGameInfo;
import static amateurs.util.InputToModelMapper.DATE_FORMATTER;
import static amateurs.util.InputToModelMapper.TIME_FORMATTER;
import static amateurs.util.StringDisplayUtil.getFormattedList;

/**
 * Supported formats:
 * 1. /invite - get general template to ask about what games to invite for
 * 2. /invite:{gameId} - create invitation(s)
 */
public class InviteCommandExecutor implements CommandExecutor {
    private static final String INVITE_TEMPLATE = """
            Which game(s) do you want to prepare an invite for?
            """;

    private static final String INVITATION_TEMPLATE = """
            Looking for %d more players for friendly doubles! %s
            
            <b>Date:</b> %s
            <b>Time:</b> %s to %s
            <b>Location:</b> %s
            <b>Court(s):</b> %s
            <b>Price/pax:</b> $%d
            <b>Max players:</b> %d
            """;

    private final ChatClient chatClient;

    private final Database database;

    private static final Logger LOG = LogManager.getLogger();

    public InviteCommandExecutor() {
        this.chatClient = ChatClient.getInstance();
        this.database = InMemoryDatabase.getInstance();
    }

    @Override
    public void executeCommand(Long chatId) {
        LOG.info("Executing invite command for chatId={}", chatId);
        final List<Game> allGames = database.getAllGames(chatId);
        if (allGames.isEmpty()) {
            chatClient.sendText(chatId, NO_GAME_TEMPLATE);
            return;
        }

        final String invitation = generateMessageWithGameInfo(INVITE_TEMPLATE, allGames);
        final List<String> gameIds = allGames.stream().map(Game::getId).toList();
        final InlineKeyboardMarkup keyboard =
                generateKeyboardForGames(INVITE_COMMAND, gameIds, true, String.valueOf(chatId));
        chatClient.sendMenu(chatId, invitation, keyboard);
    }

    @Override
    public void executeCallbackQuery(Long chatId, int msgId, String queryId, String callbackData) {
        LOG.info("Executing callback query for chatId={}, msgId={}, queryId={} and callbackData={}",
                chatId, msgId, queryId, callbackData);
        chatClient.closeCallbackQuery(queryId);

        final String[] callbackComponents = callbackData.split(COMMAND_DELIMITER);

        if (callbackComponents.length == 2) {
            if (callbackComponents[1].equals(String.valueOf(chatId))) {
                final List<Game> allGames = database.getAllGames(chatId);
                for (Game game : allGames) {
                    chatClient.sendText(chatId, generateGameInvitation(game));
                }
            } else {
                final Optional<Game> game = database.getGameById(callbackComponents[1]);
                if (game.isEmpty()) {
                    throw new IllegalArgumentException("Game does not exist!");
                }

                chatClient.sendText(chatId, generateGameInvitation(game.get()));
            }
        }
    }

    private String generateGameInvitation(Game game) {
        return String.format(INVITATION_TEMPLATE,
                game.getMaxPlayers() - game.getPlayers().size(),
                BADMINTON_EMOJI,
                DATE_FORMATTER.format(game.getDate()),
                TIME_FORMATTER.format(game.getStartTime()).toUpperCase(),
                TIME_FORMATTER.format(game.getEndTime()).toUpperCase(),
                game.getLocation(),
                getFormattedList(game.getCourts()),
                game.getPricePerPax(),
                game.getMaxPlayers());
    }
}
