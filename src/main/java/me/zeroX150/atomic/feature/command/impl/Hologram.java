package me.zeroX150.atomic.feature.command.impl;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.command.Command;
import me.zeroX150.atomic.helper.Client;
import me.zeroX150.atomic.helper.HologramManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;

public class Hologram extends Command {
    public Hologram() {
        super("Hologram", "generate a hologram", "hologram", "holo", "hlg");
    }

    @Override
    public void onExecute(String[] args) {
        if (args.length < 2) {
            Client.notifyUser("i need options and text pls. example: \".hologram eb your text\". e = spawn as egg, b = spawn as baby, n = none");
            return;
        }
        String options = args[0].toLowerCase();
        boolean generateAsBaby = false;
        boolean generateAsEgg = false;
        boolean makeGravity = false;
        boolean makeVisible = false;
        for (char c : options.toCharArray()) {
            switch (c) {
                case 'e' -> generateAsEgg = true;
                case 'b' -> generateAsBaby = true;
                case 'g' -> makeGravity = true;
                case 'v' -> makeVisible = true;
                case 'n' -> {
                }
                default -> {
                    Client.notifyUser("Unknown option \"" + c + "\". Valid options:");
                    Client.notifyUser("  N = None (Placeholder)");
                    Client.notifyUser("  E = Makes a spawn egg instead of an armor stand item");
                    Client.notifyUser("  B = Makes the hologram entity small");
                    Client.notifyUser("  G = Makes the hologram have gravity");
                    Client.notifyUser("  V = Makes the hologram entity visible");
                    return;
                }
            }
        }
        String text = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        Vec3d pos = Atomic.client.player.getPos();
        BlockPos displayable = Atomic.client.player.getBlockPos();
        Client.notifyUser("Armor stand config:");
        Client.notifyUser("  Text: " + text);
        Client.notifyUser("  Is baby: " + (generateAsBaby ? "Yes" : "No"));
        Client.notifyUser("  Is egg: " + (generateAsEgg ? "Yes" : "No"));
        Client.notifyUser("  Is invisible: " + (!makeVisible ? "Yes" : "No"));
        Client.notifyUser("  Has gravity: " + (makeGravity ? "Yes" : "No"));
        Client.notifyUser("  Pos: " + displayable.getX() + ", " + displayable.getY() + ", " + displayable.getZ());
        HologramManager.Hologram h = HologramManager.generate(text, pos).withEgg(generateAsEgg).withChild(generateAsBaby).withGravity(makeGravity).withVisible(makeVisible);
        ItemStack stack = h.generate();
        Client.notifyUser("Dont forget to open your inventory before placing");
        Atomic.client.player.getInventory().addPickBlock(stack);
    }
}
