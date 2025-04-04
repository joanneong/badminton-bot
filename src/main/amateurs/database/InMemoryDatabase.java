package amateurs.database;

import amateurs.model.Game;
import amateurs.model.Player;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryDatabase implements Database {
    final static Map<Long, List<Game>> sortedGamesForChatId = new HashMap<>();
    final static Map<String, Game> allGames = new HashMap<>();
    final static Map<String, List<Player>> gameToPlayersMap = new HashMap<>();

    private static final InMemoryDatabase inMemoryDatabase = new InMemoryDatabase();

    static {
        populateWithDummyData();
    }

    private InMemoryDatabase() {

    }

    public static InMemoryDatabase getInstance() {
        return inMemoryDatabase;
    }

    @Override
    public List<Game> getAllGames(Long chatId) {
        List<Game> games = sortedGamesForChatId.getOrDefault(chatId, new ArrayList<>());
        games.forEach(game -> game.setPlayers(gameToPlayersMap.get(game.getId())));
        Collections.sort(games);
        return games;
    }

    @Override
    public List<String> getAllGameIds(Long chatId) {
        List<Game> games = sortedGamesForChatId.getOrDefault(chatId, new ArrayList<>());
        Collections.sort(games);
        return games.stream().map(Game::getId).toList();
    }

    @Override
    public Optional<Game> getGameById(String gameId) {
        Game game = allGames.get(gameId);
        if (game == null) {
            return Optional.empty();
        }

        game.setPlayers(gameToPlayersMap.get(game.getId()));
        return Optional.of(game);
    }

    @Override
    public List<Game> addGame(Long chatId, Game newGame) {
        final List<Game> savedGames = sortedGamesForChatId.getOrDefault(chatId, new ArrayList<>());
        savedGames.add(newGame);
        sortedGamesForChatId.put(chatId, savedGames);
        allGames.put(newGame.getId(), newGame);
        gameToPlayersMap.put(newGame.getId(), new ArrayList<>());
        return savedGames;
    }

    @Override
    public Game addPlayersToGame(String gameId, List<String> players) {
        Game savedGame = allGames.get(gameId);
        if (savedGame == null) {
            return null;
        }
        List<Player> newPlayers = players.stream().map(Player::new).toList();
        gameToPlayersMap.get(savedGame.getId()).addAll(newPlayers);
        savedGame.setPlayers(gameToPlayersMap.get(savedGame.getId()));
        return savedGame;
    }

    @Override
    public List<Game> editGame(Long chatId, int index, Game editedGame) {
        if (sortedGamesForChatId.get(chatId) == null) {
            return null;
        }

        final List<Game> savedGames = sortedGamesForChatId.get(chatId);
        if (index > savedGames.size()) {
            return null;
        }

        final Game gameToEdit = savedGames.get(index - 1);
        gameToEdit.setDate(editedGame.getDate());
        gameToEdit.setStartTime(editedGame.getStartTime());
        gameToEdit.setEndTime(editedGame.getEndTime());
        gameToEdit.setLocation(editedGame.getLocation());
        gameToEdit.setCourts(editedGame.getCourts());
        gameToEdit.setPlayers(editedGame.getPlayers());
        return sortedGamesForChatId.get(chatId);
    }

    @Override
    public boolean deleteGame(Long chatId, String gameId) {
        final Game game = allGames.get(gameId);
        if (game == null) {
            return false;
        }

        allGames.remove(gameId);
        final List<Game> games = sortedGamesForChatId.get(chatId);
        games.remove(game);
        gameToPlayersMap.remove(gameId);
        return true;
    }

    private static void populateWithDummyData() {
        final ZonedDateTime now = ZonedDateTime.now();
        final List<Game> dummyGames = new ArrayList<>();
        final String dummyGameId = "someDummyGameId";
        final Game dummyGame = Game.builder()
                .id(dummyGameId)
                .date(now.toLocalDate().plusDays(3))
                .startTime(now.toLocalTime().plusHours(1))
                .endTime(now.toLocalTime().plusHours(3))
                .location("CCK Sports Hall")
                .courts(List.of("1"))
                .maxPlayers(6)
                .pricePerPax(6)
                .build();
        dummyGames.add(dummyGame);
        sortedGamesForChatId.put(197040891L, dummyGames);
        allGames.put(dummyGameId, dummyGame);
        final List<Player> players = new ArrayList<>();
        players.add(new Player("Jojopup123"));
        gameToPlayersMap.put(dummyGameId, players);
    }
}
