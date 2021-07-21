package me.zeroX150.atomic.feature.command.impl;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.command.Command;
import me.zeroX150.atomic.helper.Client;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.text.Text;

public class ViewNbt extends Command {
    int i = 0;

    public ViewNbt() {
        super("ViewNbt", "Views the nbt data of the current item", "viewnbt", "shownbt");
    }

    @Override
    public void onExecute(String[] args) {
        boolean formatted = false;
        boolean copy = false;
        boolean noColor = false;
        if (args.length == 0) {
            Client.notifyUser("pro tip: use \"viewnbt help\" to show additional options");
        } else if (args[0].equalsIgnoreCase("help")) {
            Client.notifyUser("Use flags like these to control what to do with the nbt:");
            Client.notifyUser("  N - Nothing (to skip the help message)");
            Client.notifyUser("  F - Formatted (to format the nbt in a nice way)");
            Client.notifyUser("  C - Copy (to copy the nbt to clipboard)");
            Client.notifyUser("  W - White (to show uncolored nbt)");
            Client.notifyUser("Examples: \".viewnbt FC\" to view a formatted view of the nbt and to copy it to clipboard");
            Client.notifyUser("\".viewnbt CW\" to copy the nbt and show it without colors");
            return;
        } else {
            for (char c : args[0].toLowerCase().toCharArray()) {
                switch (c) {
                    case 'n' -> {
                    }
                    case 'f' -> formatted = true;
                    case 'c' -> copy = true;
                    case 'w' -> noColor = true;
                    default -> {
                        Client.notifyUser("Unknown option '" + c + "'.");
                        return;
                    }
                }
            }
        }
        if (Atomic.client.player.getInventory().getMainHandStack().isEmpty()) {
            Client.notifyUser("you're not holding anything");
            return;
        }
        ItemStack stack = Atomic.client.player.getInventory().getMainHandStack();
        NbtCompound c = stack.getTag();
        if (!stack.hasTag() || c == null) {
            Client.notifyUser("stack has no data");
            return;
        }
        if (formatted) {
            parse(c, "(root)");
        } else {
            // gotta use .sendMessage because of monkey minecraft api
            if (noColor) {
                Atomic.client.player.sendMessage(Text.of(c.asString()), false);
            } else {
                Atomic.client.player.sendMessage(NbtHelper.toPrettyPrintedText(c), false);
            }
        }
        if (copy) {
            Atomic.client.keyboard.setClipboard(c.asString());
            Client.notifyUser("Copied nbt!");
        }
    }

    void parse(NbtElement ne, String componentName) {
        if (ne instanceof NbtByteArray || ne instanceof NbtCompound || ne instanceof NbtIntArray || ne instanceof NbtList || ne instanceof NbtLongArray) {
            Client.notifyUser(" ".repeat(i) + (componentName == null ? "-" : componentName + ":"));
            i += 2;
            if (ne instanceof NbtByteArray array) {
                for (NbtByte nbtByte : array) {
                    parse(nbtByte, null);
                }
            } else if (ne instanceof NbtCompound compound) {
                for (String key : compound.getKeys()) {
                    NbtElement ne1 = compound.get(key);
                    parse(ne1, key);
                }
            } else if (ne instanceof NbtIntArray nbtIntArray) {
                for (NbtInt nbtInt : nbtIntArray) {
                    parse(nbtInt, null);
                }
            } else if (ne instanceof NbtList nbtList) {
                for (NbtElement nbtElement : nbtList) {
                    parse(nbtElement, null);
                }
            } else {
                NbtLongArray nbtLongArray = (NbtLongArray) ne;
                for (NbtLong nbtLong : nbtLongArray) {
                    parse(nbtLong, null);
                }
            }
            i -= 2;
        } else {
            Client.notifyUser(" ".repeat(i) + (componentName == null ? "-" : componentName + ":") + " " + ne.toString().replaceAll("§", "&"));
        }
    }
}
