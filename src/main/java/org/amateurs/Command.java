package org.amateurs;

import lombok.Getter;
import org.apache.commons.lang3.stream.Streams;

import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Getter
public enum Command {
    START_COMMAND ("/start"),
    LIST_COMMAND ("/list"),
    ADD_COMMAND ("/add"),
    EDIT_COMMAND ("/edit"),
    DELETE_COMMAND ("/delete"),
    JOIN_COMMAND ("/join"),
    INVITE_COMMAND ("/invite");

    private final String command;

    private static final Map<String, Command> COMMAND_MAP;

    public static final String COMMAND_DELIMITER = ":";

    Command(String command) {
        this.command = command;
    }

    static {
        COMMAND_MAP = Streams.of(Command.values())
                .collect(toMap(Command::getCommand, identity()));
    }

    public static Command get(String value) {
        return COMMAND_MAP.get(value.toLowerCase());
    }
}
