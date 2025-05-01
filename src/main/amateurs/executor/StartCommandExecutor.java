package amateurs.executor;

import amateurs.Command;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import static amateurs.Command.START_COMMAND;
import static amateurs.util.CommandExecutorUtil.BADMINTON_EMOJI;

public class StartCommandExecutor extends BaseCommandExecutor implements CommandExecutor {
    private static final String HELP_TEMPLATE = """
                Welcome to BadmintonBot! %s
                
                What do you want to do today?
                """;

    private final InlineKeyboardMarkup startMenu;

    public StartCommandExecutor() {
        super();
        final InlineKeyboardMarkup.InlineKeyboardMarkupBuilder<?, ?> keyboardMarkupBuilder = InlineKeyboardMarkup.builder();

        for (Command command : Command.values()) {
            if (command != START_COMMAND) {
                final InlineKeyboardButton inlineKeyboardButton = InlineKeyboardButton.builder()
                        .text(command.getDescription())
                        .callbackData(command.getCommand())
                        .build();
                keyboardMarkupBuilder.keyboardRow(new InlineKeyboardRow(inlineKeyboardButton));
            }
        }

        this.startMenu = keyboardMarkupBuilder.build();
    }

    @Override
    public void executeCommand(Long chatId) {
        final String helpMessage = String.format(HELP_TEMPLATE, BADMINTON_EMOJI);
        chatClient.sendMenu(chatId, helpMessage, startMenu);
    }

    @Override
    public void executeCallbackQuery(Long chatId, int msgId, String queryId, String callbackData) {
        throw new UnsupportedOperationException("start command does not have a use case for callback query");
    }
}
