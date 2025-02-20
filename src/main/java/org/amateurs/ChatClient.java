package org.amateurs;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static org.amateurs.Main.BOT_TOKEN_KEY;

public class ChatClient {
    private final TelegramClient client;

    private static final ChatClient chatClient = new ChatClient();

    private ChatClient() {
        this.client = new OkHttpTelegramClient(System.getenv(BOT_TOKEN_KEY));
    }

    public static ChatClient getInstance() {
        return chatClient;
    }

    public void sendText(Long chatId, String msg) {
        final SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(msg)
                .parseMode("HTML")
                .build();
        try {
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMenu(Long chatId, String txt, InlineKeyboardMarkup kb) {
        SendMessage msg = SendMessage.builder()
                .chatId(chatId)
                .parseMode("HTML")
                .text(txt)
                .replyMarkup(kb)
                .build();

        try {
            client.execute(msg);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeCallbackQuery(String queryId) {
        final AnswerCallbackQuery callbackQuery = AnswerCallbackQuery.builder()
                .callbackQueryId(queryId).build();

        try {
            client.execute(callbackQuery);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
