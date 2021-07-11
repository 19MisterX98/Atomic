package me.zeroX150.atomic.feature.command.impl;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.command.Command;
import me.zeroX150.atomic.helper.Client;
import net.minecraft.client.network.PlayerListEntry;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ForEach extends Command {
    ExecutorService runner = Executors.newFixedThreadPool(1);

    public ForEach() {
        super("ForEach", "Do something for each player", "foreach", "for", "fe");
    }

    @Override
    public void onExecute(String[] args) {
        if (args.length < 2) {
            Client.notifyUser("Syntax: foreach (delayMS) (message)");
            Client.notifyUser("%s in the message gets replaced with the player name");
            Client.notifyUser("Example: .foreach 1000 /msg %s Hi %s! I think you're fucking retarded");
            return;
        }
        int delay = Client.tryParseInt(args[0], -1);
        if (delay < 0) {
            Client.notifyUser("I need a valid int above 0 please");
            return;
        }
        Client.notifyUser("Sending the message for every person in the player list, with " + delay + "ms delay");
        for (PlayerListEntry playerListEntry : Atomic.client.getNetworkHandler().getPlayerList()) {
            if (Client.isPlayerNameValid(playerListEntry.getProfile().getName()) && !playerListEntry.getProfile().getId().equals(Atomic.client.player.getUuid())) {
                runner.execute(() -> {
                    try {
                        Atomic.client.player.sendChatMessage(String.join(" ", Arrays.copyOfRange(args, 1, args.length)).replaceAll("%s", playerListEntry.getProfile().getName()));
                        Thread.sleep(delay);
                    } catch (Exception ignored) {}
                });
            }
        }
    }
}
