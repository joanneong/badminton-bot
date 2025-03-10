package org.amateurs.executor;

import org.amateurs.Command;

public class CommandExecutorFactory {
    public static CommandExecutor getCommandExecutor(Command command) {
        return switch (command) {
            case START_COMMAND -> new StartCommandExecutor();
            case LIST_COMMAND -> new ListCommandExecutor();
            case ADD_COMMAND -> new AddCommandExecutor();
            case EDIT_COMMAND -> new EditCommandExecutor();
            case DELETE_COMMAND -> new DeleteCommandExecutor();
            case JOIN_COMMAND -> new JoinCommandExecutor();
        };
    }
}
