package amateurs.database;

import amateurs.model.Game;

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
     * Gets a game by its id
     * @param chatId        unique id of chat requesting for the game info
     * @param gameId        unique id of a game
     * @return              game associated with this id
     */
    Optional<Game> getGameById(Long chatId, Long gameId);

    /**
     * Add a new game and associate it with a chat id
     * @param chatId        chat id of the chat (individual or group)
     * @param newGame       details of the new game
     * @return              game saved in the database
     */
    Game addGame(Long chatId, Game newGame);

    /**
     * Add players to a game with a game id
     * @param gameId        game id of the game
     * @param players       players to add to the game
     * @return              the updated game details
     */
    Game addPlayersToGame(Long gameId, List<String> players);

    /**
     * Delete a game belonging to a chat
     * @param chatId        chat id of the chat (individual or group)
     * @param gameId        unique identifier of the game to delete
     * @return              boolean indicating whether the delete operation was completed successfully
     */
    boolean deleteGame(Long chatId, Long gameId);
}
