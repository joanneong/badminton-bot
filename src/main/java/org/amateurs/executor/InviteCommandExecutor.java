package org.amateurs.executor;

import org.amateurs.ChatClient;
import org.amateurs.database.Database;
import org.amateurs.database.InMemoryDatabase;
import org.amateurs.model.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.amateurs.Command.COMMAND_DELIMITER;
import static org.amateurs.Command.INVITE_COMMAND;
import static org.amateurs.components.OptionsKeyboardBuilder.buildOptionsKeyboard;
import static org.amateurs.executor.ListCommandExecutor.NO_GAME_TEMPLATE;

/**
 * Supported formats:
 * 1. /invite - get general template to ask about what games to invite for
 * 2. /invite:{gameId} - create invitation(s)
 */
public class InviteCommandExecutor implements CommandExecutor {
    private static final String INVITE_TEMPLATE = """
            Which game(s) do you want to prepare an invite for?
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

        final StringBuilder invitation = new StringBuilder(INVITE_TEMPLATE);
        final List<String> gameNames = new ArrayList<>();
        final List<String> gameIds = new ArrayList<>();
        for (int i = 0; i < allGames.size(); i++) {
            invitation.append("\n");
            invitation.append(allGames.get(i).toDisplayString(i + 1));
            gameNames.add(String.format("Game %d", i + 1));
            gameIds.add(String.join(COMMAND_DELIMITER, INVITE_COMMAND.getCommand(), allGames.get(i).getId()));
        }

        if (allGames.size() > 1) {
            gameNames.add("All games");
            gameIds.add(String.join(COMMAND_DELIMITER, INVITE_COMMAND.getCommand(), String.valueOf(chatId)));
        }

        final InlineKeyboardMarkup keyboard = buildOptionsKeyboard(gameNames, gameIds, 3);
        chatClient.sendMenu(chatId, invitation.toString(), keyboard);
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
                    chatClient.sendText(chatId, game.toInvitationString());
                }
            } else {
                final Optional<Game> game = database.getGameById(callbackComponents[1]);
                if (game.isEmpty()) {
                    throw new IllegalArgumentException("Game does not exist!");
                }

                chatClient.sendText(chatId, game.get().toInvitationString());
            }
        }
    }
}
