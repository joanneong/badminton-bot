package org.amateurs;

import lombok.Getter;
import org.apache.commons.lang3.stream.Streams;

import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Getter
public enum Command {
    START_COMMAND ("/start", "Get started here"),
    LIST_COMMAND ("/list", "List all games"),
    ADD_COMMAND ("/add", "Add a game"),
    DELETE_COMMAND ("/delete", "Delete a game"),
    JOIN_COMMAND ("/join", "Add player(s) to a game"),
    INVITE_COMMAND ("/invite", "Create invitation for a game");

    private final String command;

    private final String description;

    private static final Map<String, Command> COMMAND_MAP;

    public static final String COMMAND_DELIMITER = ":";

    Command(String command, String description) {
        this.command = command;
        this.description = description;
    }

    static {
        COMMAND_MAP = Streams.of(Command.values())
                .collect(toMap(Command::getCommand, identity()));
    }

    public static Command get(String value) {
        return COMMAND_MAP.get(value.toLowerCase());
    }
}
