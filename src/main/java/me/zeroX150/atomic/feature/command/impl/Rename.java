package me.zeroX150.atomic.feature.command.impl;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.command.Command;
import me.zeroX150.atomic.helper.Client;
import net.minecraft.text.Text;

public class Rename extends Command {
    public Rename() {
        super("Rename", "Renames an item", "rename", "rn", "name");
    }

    @Override
    public void onExecute(String[] args) {
        if (args.length == 0) {
            Client.notifyUser("I need a new name dude");
            Client.notifyUser("example: rename &c&lthe &afunny");
            return;
        }
        if (Atomic.client.player.getInventory().getMainHandStack().isEmpty()) {
            Client.notifyUser("idk if you're holding anything");
            return;
        }
        Atomic.client.player.getInventory().getMainHandStack().setCustomName(Text.of("§r" + String.join(" ", args).replaceAll("&", "§")));
    }
}
