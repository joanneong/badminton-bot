package amateurs.util;

import amateurs.Command;
import amateurs.model.Game;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static amateurs.Command.COMMAND_DELIMITER;
import static amateurs.components.OptionsKeyboardBuilder.buildOptionsKeyboard;

public class CommandExecutorUtil {
    public static final String BADMINTON_EMOJI = new String(Character.toChars(
            Integer.parseInt("1F3F8", 16)));

    /**
     * Generate sequential integers in the range [inclusiveStart, inclusiveEnd] with a specified interval
     * @param inclusiveStart    inclusiveStart of range
     * @param inclusiveEnd      inclusiveEnd of range
     * @param interval          interval between each generated integer
     * @return                  list of sequential integers
     */
    public static List<String> generateSequentialInt(int inclusiveStart, int inclusiveEnd, int interval) {
        return IntStream.iterate(inclusiveStart, i -> i + interval)
                .limit((inclusiveEnd - inclusiveStart) / interval + 1)
                .mapToObj(String::valueOf)
                .toList();
    }

    /**
     * Generate an inline keyboard for all available games
     * @param command           command to include as part of game id callback data
     * @param gameIds           list of ids for all available games
     * @return                  inline keyboard with options for each game
     */
    public static InlineKeyboardMarkup generateKeyboardForGames(Command command, List<Long> gameIds) {
        return generateKeyboardForGames(command, gameIds, false, null);
    }

    /**
     * Generate an inline keyboard for all available games
     * @param command           command to include as part of game id callback data
     * @param gameIds           list of ids for all available games
     * @param hasAllGamesOption boolean flag to indicate whether there should be an additional option for all games
     * @param allGamesOptionCallback callback data for all games option
     * @return                  inline keyboard with options for each game
     */
    public static InlineKeyboardMarkup generateKeyboardForGames(Command command, List<Long> gameIds,
                                                               boolean hasAllGamesOption, String allGamesOptionCallback) {
        final List<String> gameNames = new ArrayList<>();
        final List<String> gameIdCallbacks = new ArrayList<>();
        for (int i = 0; i < gameIds.size(); i++) {
            gameNames.add(String.format("Game %d", i + 1));
            gameIdCallbacks.add(String.join(COMMAND_DELIMITER, command.getCommand(), String.valueOf(gameIds.get(i))));
        }

        if (hasAllGamesOption && gameIds.size() > 1) {
            if (allGamesOptionCallback == null) {
                throw new IllegalArgumentException("Please provide a callback for the all games option!");
            }

            gameNames.add("All games");
            gameIdCallbacks.add(String.join(COMMAND_DELIMITER, command.getCommand(), allGamesOptionCallback));
        }

        return buildOptionsKeyboard(gameNames, gameIdCallbacks, 3);
    }

    public static String generateMessageWithGameInfo(String template, List<Game> games) {
        final StringBuilder msg = new StringBuilder(template);
        for (int i = 0; i < games.size(); i++) {
            msg.append("\n");
            msg.append(games.get(i).getIndexedGameInfoString(i + 1));
        }
        return msg.toString();
    }
}
