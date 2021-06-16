package me.zeroX150.atomic.feature.command;

import me.zeroX150.atomic.feature.command.impl.*;

import java.util.ArrayList;
import java.util.List;

public class CommandRegistry {
    private static final List<Command> commands = new ArrayList<>();

    static {
        commands.add(new Test());
        commands.add(new Toggle());
        commands.add(new Config());
        commands.add(new Gamemode());
        commands.add(new Effect());
    }

    public static List<Command> getCommands() {
        return commands;
    }

    public static Command getByClass(Class<? extends Command> clazz) {
        for (Command command : getCommands()) {
            if (command.getClass() == clazz) return command;
        }
        return null;
    }

    public static Command getByAlias(String n) {
        for (Command command : getCommands()) {
            for (String alias : command.getAliases()) {
                if (alias.equalsIgnoreCase(n)) return command;
            }
        }
        return null;
    }
}
