package org.amateurs.executor;

public interface CommandExecutor {
    /**
     * Executes a command for a given chat id
     * @param chatId        unique identifier for a chat
     */
    void executeCommand(Long chatId);

    /**
     * Executes a command with some parameters for a given chat id
     * @param chatId        unique identifier for a chat
     * @param params        params provided along with command
     */
    default void executeCommandWithParams(Long chatId, String params) {
        throw new UnsupportedOperationException("This command does not accept any user input.");
    }

    /**
     * Executes a callback query
     * @param chatId        unique identifier for a chat
     * @param msgId         unique identifier for a message
     * @param queryId       unique identifier for query that triggered the callback
     * @param callbackData  data provided in callback query
     */
    void executeCallbackQuery(Long chatId, int msgId, String queryId, String callbackData);
}
