package org.amateurs.database;

import org.amateurs.model.Game;

import java.util.List;
import java.util.Optional;

public interface Database {
    /**
     * Get all games associated with a chat id that are on/after today sorted by game date/time
     * @param chatId        chat id of the chat (individual or group)
     * @return              all games associated with the chat id
     */
    List<Game> getAllGames(Long chatId);

    /**
     * Get all ids for games associated with a chat id that are on/after today
     * @param chatId        chat id of the chat (individual or group)
     * @return              ids of games associated with the chat id
     */
    List<String> getAllGameIds(Long chatId);

    /**
     * Gets a game by its id
     * @param gameId        unique id of a game
     * @return              game associated with this id
     */
    Optional<Game> getGameById(String gameId);

    /**
     * Add a new game and associate it with a chat id
     * @param chatId        chat id of the chat (individual or group)
     * @param newGame       details of the new game
     * @return              all games associated with the chat id
     */
    List<Game> addGame(Long chatId, Game newGame);

    /**
     * Add players to a game with a game id
     * @param gameId        game id of the game
     * @param players       players to add to the game
     * @return              the updated game details
     */
    Game addPlayersToGame(String gameId, List<String> players);

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
     * @param gameId        unique identifier of the game to delete
     * @return              boolean indicating whether the delete operation was completed successfully
     */
    boolean deleteGame(Long chatId, String gameId);
}
