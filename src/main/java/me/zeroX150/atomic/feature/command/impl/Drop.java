package me.zeroX150.atomic.feature.command.impl;

import me.zeroX150.atomic.feature.command.Command;
import me.zeroX150.atomic.helper.Client;

public class Drop extends Command {
    public Drop() {
        super("Drop", "Drops all items in your inv", "drop", "d", "throw");
    }

    @Override
    public void onExecute(String[] args) {
        for (int i = 0; i < 36; i++) {
            Client.drop(i);
        }
    }
}
