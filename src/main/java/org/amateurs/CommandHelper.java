package org.amateurs;

public class CommandHelper {
    public static Command extractCommand(String msg) {
        return Command.get(msg.split(Command.COMMAND_DELIMITER)[0]);
    }
}
