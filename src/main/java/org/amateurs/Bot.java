package org.amateurs;

import org.amateurs.executor.CommandExecutor;
import org.amateurs.executor.CommandExecutorFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static org.amateurs.Command.COMMAND_DELIMITER;

public class Bot implements LongPollingSingleThreadUpdateConsumer {
    private static final Logger LOG = LogManager.getLogger();

    @Override
    public void consume(Update update) {
        LOG.info("Received update={}", update);
        if (update.getMessage() != null) {
            processCommand(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            processCallbackQuery(update.getCallbackQuery());
        }
    }

    private void processCommand(Message msg) {
        LOG.info("Processing command with message={}", msg);
        final Long chatId = msg.getChatId();
        final Command command = CommandHelper.extractCommand(msg.getText());
        final CommandExecutor commandExecutor = CommandExecutorFactory.getCommandExecutor(command);

        if (msg.getText().contains(COMMAND_DELIMITER)) {
            commandExecutor.executeCommandWithParams(chatId, msg.getText());
        } else {
            commandExecutor.executeCommand(chatId);
        }
    }

    private void processCallbackQuery(CallbackQuery callbackQuery) {
        LOG.info("Processing callback query with query={}", callbackQuery);
        final Long chatId = callbackQuery.getMessage().getChatId();
        final int msgId = callbackQuery.getMessage().getMessageId();
        final String queryId = callbackQuery.getId();
        final String callbackData = callbackQuery.getData();
        final Command command = CommandHelper.extractCommand(callbackData);
        final CommandExecutor commandExecutor = CommandExecutorFactory.getCommandExecutor(command);
        commandExecutor.executeCallbackQuery(chatId, msgId, queryId, callbackData);
    }
}
