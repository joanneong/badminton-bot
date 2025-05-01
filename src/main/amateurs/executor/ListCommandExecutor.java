package amateurs.executor;

import amateurs.model.Game;

import java.util.List;

public class ListCommandExecutor extends BaseCommandExecutor implements CommandExecutor {
    private static final String LIST_TEMPLATE = """
            Here are all your upcoming games:
            """;

    protected static final String NO_GAME_TEMPLATE = """
            You do not have any upcoming games :( How can???
            """;

    public ListCommandExecutor() {
        super();
    }

    @Override
    public void executeCommand(Long chatId) {
        final List<Game> allGames = database.getAllGames(chatId);
        if (allGames.isEmpty()) {
            chatClient.sendText(chatId, NO_GAME_TEMPLATE);
            return;
        }

        final StringBuilder allGameDetails = new StringBuilder(LIST_TEMPLATE);
        for (int i = 0; i < allGames.size(); i++) {
            allGameDetails.append("\n");
            allGameDetails.append(allGames.get(i).getIndexedFullGameInfoString(i + 1));
        }

        chatClient.sendText(chatId, allGameDetails.toString());
    }

    @Override
    public void executeCallbackQuery(Long chatId, int msgId, String queryId, String callbackData) {
        chatClient.closeCallbackQuery(queryId);
        executeCommand(chatId);
    }
}
