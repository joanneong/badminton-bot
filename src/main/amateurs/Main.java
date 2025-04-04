package amateurs;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main {
    public static final String BOT_TOKEN_KEY = "BOT_TOKEN";

    public static void main(String[] args) {
        try {
            final String botToken = System.getenv(BOT_TOKEN_KEY);
            TelegramBotsLongPollingApplication botsApp = new TelegramBotsLongPollingApplication();
            final Bot bot = new Bot();
            botsApp.registerBot(botToken, bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}