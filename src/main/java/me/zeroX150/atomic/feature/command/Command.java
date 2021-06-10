package me.zeroX150.atomic.feature.command;

public abstract class Command {
    private final String name;
    private final String description;
    private final String[] aliases;

    public Command(String n, String d, String... a) {
        this.name = n;
        this.description = d;
        this.aliases = a;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String[] getAliases() {
        return aliases;
    }

    public abstract void onExecute(String[] args);
}
