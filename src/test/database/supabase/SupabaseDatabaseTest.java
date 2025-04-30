package database.supabase;

import amateurs.database.Database;
import amateurs.database.supabase.SupabaseDatabase;
import amateurs.database.supabase.SupabaseHttpClient;
import amateurs.mapper.GameCourtMixin;
import amateurs.model.Game;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SupabaseDatabaseTest {
    private Database database;

    private static final Long TEST_CHAT_ID = 19704088L;

    private static final String GAME_TABLE = "dev_game";

    private static final String COURT_TABLE = "dev_court";

    private static final String PLAYER_TABLE = "dev_player";

    @BeforeEach
    public void setUp() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.addMixIn(Game.class, GameCourtMixin.class);
        database = new SupabaseDatabase(GAME_TABLE, COURT_TABLE, PLAYER_TABLE, objectMapper);
    }

    @AfterEach
    public void cleanUp() {
        final Map<String, String> queryParams = Map.of(
                "id", "gt.0"
        );
        SupabaseHttpClient.getInstance().sendDeleteRequest(GAME_TABLE, queryParams);
    }

    @Test
    public void testAddGameAndGetAllGames() {
        final Game game = buildDummyGame();
        database.addGame(TEST_CHAT_ID, game);

        final List<Game> games = database.getAllGames(TEST_CHAT_ID);
        assertNotNull(games);
        assertEquals(1, games.size());
        final Game savedGame = games.getFirst();
        assertGameFieldsArePresent(savedGame);
    }

    @Test
    public void testGetAllGamesForInvalidChatId_returnsEmptyList() {
        final List<Game> games = database.getAllGames(0L);
        assertNotNull(games);
        assertTrue(games.isEmpty());
    }

    @Test
    public void testAddGameAndGetGameById() {
        final Optional<Game> retrievedGame = addGameAndGetGameById();
        assertTrue(retrievedGame.isPresent());
        assertGameFieldsArePresent(retrievedGame.get());
    }

    @Test
    public void testGetGameById_forInvalidGameId_throwsException() {
        assertThrows(RuntimeException.class, () -> database.getGameById(TEST_CHAT_ID, 0L));
    }

    @Test
    public void testGetGameById_forInvalidChatId_throwsException() {
        final Game game = buildDummyGame();
        final Game savedGame = database.addGame(TEST_CHAT_ID, game);
        assertThrows(RuntimeException.class, () -> database.getGameById(0L, savedGame.getId()));
    }

    @Test
    public void testDeleteGame() {
        final Optional<Game> addedGame = addGameAndGetGameById();
        assertTrue(addedGame.isPresent());
        database.deleteGame(TEST_CHAT_ID, addedGame.get().getId());
        assertThrows(RuntimeException.class, () -> database.getGameById(TEST_CHAT_ID, addedGame.get().getId()));
    }

    private Optional<Game> addGameAndGetGameById() {
        final Game game = buildDummyGame();
        final Game savedGame = database.addGame(TEST_CHAT_ID, game);
        return database.getGameById(TEST_CHAT_ID, savedGame.getId());
    }

    private void assertGameFieldsArePresent(Game game) {
        assertNotNull(game.getId());
        assertNotNull(game.getDate());
        assertNotNull(game.getStartTime());
        assertNotNull(game.getEndTime());
        assertNotNull(game.getLocation());
        assertNotNull(game.getCourts());
        assertNotNull(game.getPlayers());
    }

    private Game buildDummyGame() {
        final ZonedDateTime now = ZonedDateTime.now();
        return Game.builder()
                .chatId(TEST_CHAT_ID)
                .date(now.toLocalDate().plusDays(3))
                .startTime(now.toLocalTime().plusHours(1))
                .endTime(now.toLocalTime().plusHours(3))
                .location("CCK Sports Hall")
                .courts(List.of("1", "2"))
                .maxPlayers(6)
                .pricePerPax(6)
                .build();
    }
}
