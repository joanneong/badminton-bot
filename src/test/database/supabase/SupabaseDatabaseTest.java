package database.supabase;

import amateurs.database.Database;
import amateurs.database.supabase.SupabaseDatabase;
import amateurs.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SupabaseDatabaseTest {
    private Database database;

    private static final Long TEST_CHAT_ID = 19704089L;

    private static final Long TEST_GAME_ID = 1L;

    @BeforeEach
    public void setUp() {
        database = new SupabaseDatabase();
    }

    @Test
    public void testGetAllGames() {
        final List<Game> games = database.getAllGames(TEST_CHAT_ID);
        assertNotNull(games);
        assertEquals(1, games.size());
        final Game game = games.getFirst();
        assertGameFieldsArePresent(game);
    }

    @Test
    public void testGetAllGames_returnsEmptyList() {
        final List<Game> games = database.getAllGames(0L);
        assertNotNull(games);
        assertTrue(games.isEmpty());
    }

    @Test
    public void testGetGameById() {
        final Optional<Game> game = database.getGameById(TEST_CHAT_ID, TEST_GAME_ID);
        assertTrue(game.isPresent());
        assertGameFieldsArePresent(game.get());
    }

    @Test
    public void testGetGameById_forInvalidGameId_throwsException() {
        assertThrows(RuntimeException.class, () -> database.getGameById(TEST_CHAT_ID, 0L));
    }

    @Test
    public void testGetGameById_forInvalidChatId_throwsException() {
        assertThrows(RuntimeException.class, () -> database.getGameById(0L, TEST_GAME_ID));
    }

    @Test
    public void testAddGame() {

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
}
