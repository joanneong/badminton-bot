package org.amateurs.database;

import org.amateurs.model.Game;

import java.util.List;

public interface Database {
    /**
     * Get all games associated with a chat id that are on/after today sorted by game date/time
     * @param chatId        chat id of the chat (individual or group)
     * @return              all games associated with the chat id
     */
    List<Game> getAllGames(Long chatId);

    /**
     * Add a new game and associate it with a chat id
     * @param chatId        chat id of the chat (individual or group)
     * @param newGame       details of the new game
     * @return              all games associated with the chat id
     */
    List<Game> addGame(Long chatId, Game newGame);

    /**
     * Edit a game belonging to a chat
     * @param chatId        chat id of the chat (individual or group)
     * @param index         index of game to edit (1-based indexing)
     * @param editedGame    updated details of edited game
     * @return              all games associated with the chat id
     */
    List<Game> editGame(Long chatId, int index, Game editedGame);

    /**
     * Delete a game belonging to a chat
     * @param chatId        chat id of the chat (individual or group)
     * @param index         index of the game to delete (1-based indexing)
     * @return              all games associated with the chat id
     */
    List<Game> deleteGame(Long chatId, int index);
}
