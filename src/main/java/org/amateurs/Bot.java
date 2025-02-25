package org.amateurs;

import org.amateurs.executor.CommandExecutor;
import org.amateurs.executor.CommandExecutorFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class Bot implements LongPollingSingleThreadUpdateConsumer {
    private static final Logger LOG = LogManager.getLogger();

    @Override
    public void consume(Update update) {
        LOG.info("Received update={}", update);
        if (update.getMessage() != null && update.getMessage().isCommand()) {
            processCommand(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            processCallbackQuery(update.getCallbackQuery());
        }
    }

    private void processCommand(Message msg) {
        final Long chatId = msg.getChatId();
        final Command command = Command.get(msg.getText());
        final CommandExecutor commandExecutor = CommandExecutorFactory.getCommandExecutor(command);
        commandExecutor.executeCommand(chatId);
    }

    private void processCallbackQuery(CallbackQuery callbackQuery) {
        final Long chatId = callbackQuery.getMessage().getChatId();
        final int msgId = callbackQuery.getMessage().getMessageId();
        final String queryId = callbackQuery.getId();
        final String callbackData = callbackQuery.getData();
        final Command command = CommandHelper.extractCommand(callbackData);
        final CommandExecutor commandExecutor = CommandExecutorFactory.getCommandExecutor(command);
        commandExecutor.executeCallbackQuery(chatId, msgId, queryId, callbackData);
    }
}
