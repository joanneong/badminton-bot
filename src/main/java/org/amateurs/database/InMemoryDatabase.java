package org.amateurs.database;

import org.amateurs.model.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryDatabase implements Database {
    final Map<Long, List<Game>> sortedGamesForChatId = new HashMap<>();

    @Override
    public List<Game> getAllGames(Long chatId) {
        return sortedGamesForChatId.get(chatId);
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
}
