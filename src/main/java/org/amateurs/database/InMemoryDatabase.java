package org.amateurs.database;

import org.amateurs.model.Game;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryDatabase implements Database {
    final static Map<Long, List<Game>> sortedGamesForChatId = new HashMap<>();

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
        Collections.sort(games);
        return games;
    }

    @Override
    public List<Game> addGame(Long chatId, Game newGame) {
        final List<Game> savedGames = sortedGamesForChatId.getOrDefault(chatId, new ArrayList<>());
        savedGames.add(newGame);
        sortedGamesForChatId.put(chatId, savedGames);
        return savedGames;
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
    public List<Game> deleteGame(Long chatId, int index) {
        final List<Game> savedGames = sortedGamesForChatId.get(chatId);
        if (index > savedGames.size()) {
            return null;
        }

        savedGames.remove(index - 1);
        return savedGames;
    }

    private static void populateWithDummyData() {
        final ZonedDateTime now = ZonedDateTime.now();
        final List<Game> dummyGames = new ArrayList<>();
        final Game dummyGame = Game.builder()
                .date(now.plusDays(3))
                .startTime(now.plusDays(3).plusHours(1))
                .endTime(now.plusDays(3).plusHours(3))
                .location("CCK Sports Hall")
                .courts(List.of("1"))
                .players(List.of("Jojopup123"))
                .build();
        dummyGames.add(dummyGame);
        sortedGamesForChatId.put(197040891L, dummyGames);
    }
}
