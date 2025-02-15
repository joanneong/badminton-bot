package org.amateurs;

import org.apache.commons.lang3.stream.Streams;

import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public enum Command {
    START_COMMAND ("/start"),
    LIST_COMMAND ("/list");

    private final String command;

    private static final Map<String, Command> COMMAND_MAP;

    Command(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    static {
        COMMAND_MAP = Streams.of(Command.values())
                .collect(toMap(Command::getCommand, identity()));
    }

    public static Command get(String value) {
        return COMMAND_MAP.get(value.toLowerCase());
    }
}
