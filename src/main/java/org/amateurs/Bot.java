package org.amateurs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static org.amateurs.Main.BOT_TOKEN_KEY;

public class Bot implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient client;

    private static final Logger LOG = LogManager.getLogger();

    private static final String BADMINTON_EMOJI = new String(Character.toChars(
            Integer.parseInt("1F3F8", 16)));

    private static final String START_COMMAND = "/start";

    public Bot() {
        this.client = new OkHttpTelegramClient(System.getenv(BOT_TOKEN_KEY));
    }

    @Override
    public void consume(Update update) {
        final Message msg = update.getMessage();
        final Long chatId = update.getMessage().getChatId();

        if (msg.isCommand()) {
            switch (msg.getText()) {
                case START_COMMAND -> help(chatId);
            }
        }
    }

    private void help(Long chatId) {
        LOG.info("Processing the help command for chatId: {}", chatId);
        final String helpTemplate = """
                Welcome to BadmintonBot! %s
                
                Use one of the buttons below to get started
                """;
        final String helpMessage = String.format(helpTemplate, BADMINTON_EMOJI);
        sendText(chatId, helpMessage);
    }

    private void sendText(Long chatId, String msg) {
        final SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(msg)
                .build();
        try {
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
