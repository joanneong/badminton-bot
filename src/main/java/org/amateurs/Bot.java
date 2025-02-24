package org.amateurs;

import org.amateurs.executor.AddCommandExecutor;
import org.amateurs.executor.ListCommandExecutor;
import org.amateurs.executor.StartCommandExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class Bot implements LongPollingSingleThreadUpdateConsumer {
    private static final Logger LOG = LogManager.getLogger();

    private final StartCommandExecutor startCommandExecutor;

    private final ListCommandExecutor listCommandExecutor;

    private final AddCommandExecutor addCommandExecutor;

    public Bot() {
        this.startCommandExecutor = new StartCommandExecutor();
        this.listCommandExecutor = new ListCommandExecutor();
        this.addCommandExecutor = new AddCommandExecutor();
    }

    @Override
    public void consume(Update update) {
        LOG.info("Received update={}", update);
        if (update.getMessage() != null && update.getMessage().isCommand()) {
            final Message msg = update.getMessage();
            final Long chatId = update.getMessage().getChatId();

            switch (Command.get(msg.getText())) {
                case START_COMMAND -> startCommandExecutor.executeStartCommand(chatId);
                case LIST_COMMAND -> listCommandExecutor.executeListCommand(chatId);
                case ADD_COMMAND -> addCommandExecutor.executeAddCommand(chatId);
            }
        } else if (update.hasCallbackQuery()) {
            final Long chatId = update.getCallbackQuery().getMessage().getChatId();
            final String queryId = update.getCallbackQuery().getId();
            final String callbackData = update.getCallbackQuery().getData();

            switch (Command.get(callbackData)) {
                case LIST_COMMAND -> listCommandExecutor.executeListCommand(chatId, queryId);
                case ADD_COMMAND -> addCommandExecutor.executeAddCommand(chatId, queryId);
            }
        }
    }
}
