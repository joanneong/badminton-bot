package org.amateurs.executor;

public interface CommandExecutor {
    /**
     * Executes a command for a given chat id
     * @param chatId        unique identifier for a chat
     */
    void executeCommand(Long chatId);

    /**
     * Executes a callback query
     * @param chatId        unique identifier for a chat
     * @param msgId         unique identifier for a message
     * @param queryId       unique identifier for query that triggered the callback
     * @param callbackData  data provided in callback query
     */
    void executeCallbackQuery(Long chatId, int msgId, String queryId, String callbackData);
}
