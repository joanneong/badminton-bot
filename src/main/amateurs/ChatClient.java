package amateurs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.polls.input.InputPollOption;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.Serializable;
import java.util.List;

import static amateurs.Main.BOT_TOKEN_KEY;

public class ChatClient {
    private final TelegramClient client;

    private static final ChatClient chatClient = new ChatClient();

    private static final Logger LOG = LogManager.getLogger();

    private ChatClient() {
        this.client = new OkHttpTelegramClient(System.getenv(BOT_TOKEN_KEY));
    }

    public static ChatClient getInstance() {
        return chatClient;
    }

    public void sendText(Long chatId, String msg) {
        sendMenu(chatId, msg, null);
    }

    public void sendMenu(Long chatId, String txt, InlineKeyboardMarkup kb) {
        final SendMessage msgWithMenu = SendMessage.builder()
                .chatId(chatId)
                .parseMode("HTML")
                .text(txt)
                .replyMarkup(kb)
                .build();
        execute(msgWithMenu);
    }

    public void editMessageWithMenu(Long chatId, int msgId, String txt, InlineKeyboardMarkup kb) {
        final EditMessageText editMsgWithMenu = EditMessageText.builder()
                .chatId(chatId)
                .messageId(msgId)
                .text(txt)
                .replyMarkup(kb)
                .parseMode("HTML")
                .build();
        execute(editMsgWithMenu);
    }

    public String sendMessageWithPoll(Long chatId, String question, List<InputPollOption> options) {
        final SendPoll poll = SendPoll.builder()
                .chatId(chatId)
                .allowMultipleAnswers(false)
                .question(question)
                .questionParseMode("HTML")
                .options(options)
                .isAnonymous(false)
                .isClosed(false)
                .build();
        final var response = execute(poll);
        return response.getPoll().getId();
    }

    public void closeCallbackQuery(String queryId) {
        final AnswerCallbackQuery callbackQuery = AnswerCallbackQuery.builder()
                .callbackQueryId(queryId).build();
        execute(callbackQuery);
    }

    private <T extends Serializable> T execute(BotApiMethod<? extends T> botApiMethod) {
        try {
            return client.execute(botApiMethod);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
