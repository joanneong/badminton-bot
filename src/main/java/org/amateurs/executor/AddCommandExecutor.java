package org.amateurs.executor;

import org.amateurs.ChatClient;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.amateurs.Command.ADD_COMMAND;

public class AddCommandExecutor implements CommandExecutor {
    private static final String ADD_TEMPLATE = """
            Please provide your game details :)
            
            When is the game going to be?
            """;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd LLL yyyy (EEE)");

    private final ChatClient chatClient;

    public AddCommandExecutor() {
        this.chatClient = ChatClient.getInstance();
    }

    @Override
    public void executeCommand(Long chatId) {
        chatClient.sendMenu(chatId, ADD_TEMPLATE, buildDateOptionsKeyboard());
    }

    @Override
    public void executeCallbackQuery(Long chatId, int msgId, String queryId, String callbackData) {
        chatClient.closeCallbackQuery(queryId);
        executeCommand(chatId);
    }

    private InlineKeyboardMarkup buildDateOptionsKeyboard() {
        final ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Kuala_Lumpur"));

        // 14 options in total. There are 3 rows, each row has 5 options
        final List<List<InlineKeyboardButton>> dateButtons = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            dateButtons.add(new ArrayList<>());
        }

        for (int i = 0; i < 15; i++) {
            final List<InlineKeyboardButton> dateRow = dateButtons.get(i / 5);
            final InlineKeyboardButton dateButton = buildDateButton(now.plusDays(i + 1));
            dateRow.add(dateButton);
        }

        final List<InlineKeyboardRow> dateRows = dateButtons.stream()
                .map(InlineKeyboardRow::new)
                .toList();

        return InlineKeyboardMarkup.builder()
                .keyboard(dateRows)
                .build();
    }

    private InlineKeyboardButton buildDateButton(ZonedDateTime date) {
        final String formattedDate = DATE_FORMATTER.format(date);
        return InlineKeyboardButton.builder()
                .text(formattedDate)
                .callbackData(ADD_COMMAND + ":DATE:" + formattedDate)
                .build();
    }
}
