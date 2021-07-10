package me.zeroX150.atomic.feature.command.impl;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.command.Command;
import me.zeroX150.atomic.helper.Client;
import net.minecraft.client.network.PlayerListEntry;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ForEach extends Command {
    ExecutorService runner = Executors.newFixedThreadPool(1);

    public ForEach() {
        super("ForEach", "Do something for each player", "foreach", "for", "fe");
    }

    @Override
    public void onExecute(String[] args) {
        if (args.length == 0) {
            Client.notifyUser("Syntax: foreach (message)");
            Client.notifyUser("%s in the message gets replaced with the player name");
            Client.notifyUser("Example: .foreach /msg %s Hi %s! I think you're fucking retarded");
            return;
        }
        Client.notifyUser("Sending the message for every person in the player list, with 1s delay");
        for (PlayerListEntry playerListEntry : Atomic.client.getNetworkHandler().getPlayerList()) {
            if (Client.isPlayerNameValid(playerListEntry.getProfile().getName()) && !playerListEntry.getProfile().getId().equals(Atomic.client.player.getUuid())) {
                runner.execute(() -> {
                    Atomic.client.player.sendChatMessage(String.join(" ", args).replaceAll("%s", playerListEntry.getProfile().getName()));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }
}
