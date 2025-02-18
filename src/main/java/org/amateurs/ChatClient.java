package org.amateurs;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
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

    private void buttonTap(Long chatId, String queryId, String data, int msgId) {
        EditMessageText newTxt = EditMessageText.builder()
                .chatId(chatId)
                .messageId(msgId).text("").build();

        EditMessageReplyMarkup newKb = EditMessageReplyMarkup.builder()
                .chatId(chatId).messageId(msgId).build();

        if(data.equals("next")) {
            newTxt.setText("MENU 2");
//            newKb.setReplyMarkup(keyboard.getKeyboardMarkup2());
        } else if(data.equals("back")) {
            newTxt.setText("MENU 1");
//            newKb.setReplyMarkup(keyboard.getKeyboardMarkup1());
        }

        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(queryId).build();

        try {
            client.execute(close);
            client.execute(newTxt);
            client.execute(newKb);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
