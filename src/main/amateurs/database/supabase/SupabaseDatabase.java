package amateurs.database.supabase;

import amateurs.database.Database;
import amateurs.model.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SupabaseDatabase implements Database {
    private static final Logger LOG = LogManager.getLogger();

    private static final String GAME_TABLE = "game";

    private final SupabaseHttpClient client;

    public SupabaseDatabase() {
        this.client = SupabaseHttpClient.getInstance();
    }

    @Override
    public List<Game> getAllGames(Long chatId) {
        HttpResponse<String> resp = client.sendRequest(GAME_TABLE, Map.of());
        LOG.info(resp.body());
        return List.of();
    }

    @Override
    public List<String> getAllGameIds(Long chatId) {
        return List.of();
    }

    @Override
    public Optional<Game> getGameById(String gameId) {
        return Optional.empty();
    }

    @Override
    public List<Game> addGame(Long chatId, Game newGame) {
        return List.of();
    }

    @Override
    public Game addPlayersToGame(String gameId, List<String> players) {
        return null;
    }

    @Override
    public List<Game> editGame(Long chatId, int index, Game editedGame) {
        return List.of();
    }

    @Override
    public boolean deleteGame(Long chatId, String gameId) {
        return false;
    }
}
