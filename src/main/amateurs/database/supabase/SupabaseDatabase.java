package amateurs.database.supabase;

import amateurs.database.Database;
import amateurs.model.Court;
import amateurs.model.Game;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SupabaseDatabase implements Database {
    private static final Logger LOG = LogManager.getLogger();

    private static final String GAME_TABLE = "game";

    private static final String COURT_TABLE = "court";

    private static final String PLAYER_TABLE = "player";

    private final ObjectMapper objectMapper;

    private final SupabaseHttpClient client;

    private final String gameTable;

    private final String courtTable;

    private final String playerTable;

    public SupabaseDatabase() {
        this(GAME_TABLE, COURT_TABLE, PLAYER_TABLE, new ObjectMapper());
    }

    public SupabaseDatabase(String gameTable, String courtTable, String playerTable, ObjectMapper objectMapper) {
        this.client = SupabaseHttpClient.getInstance();
        this.gameTable = gameTable;
        this.courtTable = courtTable;
        this.playerTable = playerTable;
        this.objectMapper = objectMapper;
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public List<Game> getAllGames(Long chatId) {
        final Map<String, String> queryParams = Map.of(
                "select", String.format("*,%s(court):court,%s(name):player", courtTable, playerTable),
                "chat_id", "eq." + chatId,
                "date", "gte." + LocalDate.now());
        final Optional<List<Game>> allGames = getGamesWithQuery(queryParams);
        return allGames.map(games -> {
            games.sort(Comparator.comparing(Game::getDate));
            return games;
        }).orElse(List.of());
    }

    @Override
    public Optional<Game> getGameById(Long chatId, Long gameId) {
        final Optional<List<Game>> game = checkIsValidGameOwnedByChat(chatId, gameId);
        return game.flatMap(games -> games.stream().findFirst());
    }

    @Override
    public Game addGame(Long chatId, Game newGame) {
        final Game savedGame = insertGame(newGame);
        insertCourts(savedGame.getId(), newGame.getCourts());
        return savedGame;
    }

    @Override
    public Game addPlayersToGame(Long gameId, List<String> players) {
        return null;
    }

    @Override
    public boolean deleteGame(Long chatId, Long gameId) {
        checkIsValidGameOwnedByChat(chatId, gameId);
        final Map<String, String> queryParams = Map.of("id", "eq." + gameId);
        client.sendDeleteRequest(gameTable, queryParams);
        return true;
    }

    /**
     * Checks that:
     * 1. game is associated with the chat id
     * 2. game is scheduled for the future
     * @param chatId        unique id for the chat
     * @param gameId        unique id for the game
     */
    private Optional<List<Game>> checkIsValidGameOwnedByChat(Long chatId, Long gameId) {
        final Map<String, String> queryParams = Map.of(
                "select", String.format("*,%s(court),%s(name)", courtTable, playerTable),
                "id", "eq." + gameId,
                "chat_id", "eq." + chatId,
                "date", "gte." + LocalDate.now());
        final Optional<List<Game>> game = getGamesWithQuery(queryParams);
        if (game.isEmpty() || game.get().isEmpty()) {
            throw new RuntimeException(String.format("Game with id=%d is not valid for chat with id=%d!", gameId, chatId));
        }
        return game;
    }

    private Optional<List<Game>> getGamesWithQuery(Map<String, String> queryParams) {
        try {
            final HttpResponse<String> resp = client.sendGetRequest(gameTable, queryParams);
            return Optional.of(objectMapper.readValue(resp.body(), new TypeReference<>() {}));
        } catch (Exception e) {
            LOG.error(e); // TODO: better error handling
        }
        return Optional.empty();
    }

    private Game insertGame(Game game) {
        try {
            final FilterProvider gameFilter = new SimpleFilterProvider()
                    .addFilter("supbaseGameFilter",
                            SimpleBeanPropertyFilter.serializeAllExcept("id", "court", "player"));
            final HttpResponse<String> resp = client.sendPostRequest(gameTable, objectMapper.writer(gameFilter).writeValueAsString(game));
            final List<Game> savedGames = objectMapper.readValue(resp.body(), new TypeReference<>() {});
            return savedGames.getFirst();
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new RuntimeException("Error inserting game!", e);
        }
    }

    private void insertCourts(Long gameId, List<String> gameCourts) {
        try {
            final List<Court> courts = gameCourts.stream()
                    .map(court -> new Court(gameId, court))
                    .toList();
            final FilterProvider courtFilter = new SimpleFilterProvider()
                    .addFilter("supabaseCourtFilter", SimpleBeanPropertyFilter.serializeAllExcept("id"));
            client.sendPostRequest(courtTable, objectMapper.writer(courtFilter).writeValueAsString(courts));
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new RuntimeException("Error inserting game court(s)!", e);
        }
    }
}
