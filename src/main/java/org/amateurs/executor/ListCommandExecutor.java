package org.amateurs.executor;

import org.amateurs.ChatClient;
import org.amateurs.database.Database;
import org.amateurs.database.InMemoryDatabase;
import org.amateurs.model.Game;

import java.util.List;

public class ListCommandExecutor {
    private static final String LIST_TEMPLATE = """
            Here are all your upcoming games:
            """;

    private static final String NO_GAME_TEMPLATE = """
            You do not have any upcoming games :( How can???
            """;

    private final Database database;

    private final ChatClient chatClient;

    public ListCommandExecutor() {
        this.database = new InMemoryDatabase();
        this.chatClient = ChatClient.getInstance();
    }

    public void executeListCommand(Long chatId) {
        final List<Game> allGames = database.getAllGames(chatId);
        if (allGames.isEmpty()) {
            chatClient.sendText(chatId, NO_GAME_TEMPLATE);
            return;
        }

        final StringBuilder allGameDetails = new StringBuilder(LIST_TEMPLATE);
        for (int i = 0; i < allGames.size(); i++) {
            allGameDetails.append("\n");
            allGameDetails.append(allGames.get(i).toDisplayString(i + 1));
        }

        chatClient.sendText(chatId, allGameDetails.toString());
    }

    public void executeListCommand(Long chatId, String queryId) {
        chatClient.closeCallbackQuery(queryId);
        executeListCommand(chatId);
    }
}
