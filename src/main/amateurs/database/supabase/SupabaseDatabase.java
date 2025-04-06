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

    private final SupabaseHttpClient client;

    public SupabaseDatabase() {
        this.client = SupabaseHttpClient.getInstance();
    }

    @Override
    public List<Game> getAllGames(Long chatId) {
        final Map<String, String> queryParams = Map.of(
                "select", "*,court(court),player(name)",
                "chat_id", "eq." + chatId,
                "date", "gte." + LocalDate.now());
        final HttpResponse<String> resp = client.sendRequest(GAME_TABLE, queryParams);
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());

        try {
            List<Game> g = om.readValue(resp.body(), new TypeReference<>() {});
            for (Game gg : g) {
                LOG.info(gg.getFullGameInfoString());
            }
        } catch (Exception e) {
            LOG.error(e);
        }
        LOG.info(resp.body());
        return List.of();
    }

    @Override
    public Optional<Game> getGameById(Long gameId) {
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
        return false;
    }
}
