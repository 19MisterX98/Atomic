package me.zeroX150.atomic.feature.command.impl;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.command.Command;
import me.zeroX150.atomic.helper.Client;
import net.minecraft.world.GameMode;

public class Gamemode extends Command {
    public Gamemode() {
        super("Gamemode", "Switch gamemodes client side", "gamemode", "gm", "gmode", "gamemodespoof", "gmspoof");
    }

    @Override
    public void onExecute(String[] args) {
        if (args.length == 0) {
            Client.notifyUser("gamemode pls");
        } else {
            GameMode gm = GameMode.byName(args[0]);
            Atomic.client.interactionManager.setGameMode(gm);
        }
    }
}
