package amateurs.database.supabase;

import amateurs.database.Database;
import amateurs.model.Game;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SupabaseDatabase implements Database {
    private static final Logger LOG = LogManager.getLogger();

    private static final String GAME_TABLE = "game";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final SupabaseHttpClient client;

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public SupabaseDatabase() {
        this.client = SupabaseHttpClient.getInstance();
    }

    @Override
    public List<Game> getAllGames(Long chatId) {
        final Map<String, String> queryParams = Map.of(
                "select", "*,court(court),player(name)",
                "chat_id", "eq." + chatId,
                "date", "gte." + LocalDate.now());
        final Optional<List<Game>> allGames = getGameWithQuery(queryParams);
        return allGames.orElse(List.of());
    }

    @Override
    public Optional<Game> getGameById(Long chatId, Long gameId) {
        checkIsValidGameOwnedByChat(chatId, gameId);
        return Optional.empty();
    }

    @Override
    public void addGame(Long chatId, Game newGame) {
    }

    @Override
    public Game addPlayersToGame(Long gameId, List<String> players) {
        return null;
    }

    @Override
    public boolean deleteGame(Long chatId, Long gameId) {
        checkIsValidGameOwnedByChat(chatId, gameId);
        return false;
    }

    /**
     * Checks that:
     * 1. game is associated with the chat id
     * 2. game is scheduled for the future
     * @param chatId        unique id for the chat
     * @param gameId        unique id for the game
     */
    private void checkIsValidGameOwnedByChat(Long chatId, Long gameId) {
        final Map<String, String> queryParams = Map.of(
                "select", "id",
                "id", "eq." + gameId,
                "chat_id", "eq." + chatId,
                "date", "gte." + LocalDate.now());
        final Optional<List<Game>> game = getGameWithQuery(queryParams);
        if (game.isEmpty() || game.get().isEmpty()) {
            throw new RuntimeException(String.format("Game with id=%d is not valid for chat with id=%d!", gameId, chatId));
        }
    }

    private Optional<List<Game>> getGameWithQuery(Map<String, String> queryParams) {
        try {
            final HttpResponse<String> resp = client.sendRequest(GAME_TABLE, queryParams);
            return Optional.of(objectMapper.readValue(resp.body(), new TypeReference<>() {}));
        } catch (Exception e) {
            LOG.error(e); // TODO: better error handling
        }
        return Optional.empty();
    }
}
