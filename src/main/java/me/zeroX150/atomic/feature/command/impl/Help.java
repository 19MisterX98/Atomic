package me.zeroX150.atomic.feature.command.impl;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.command.Command;
import me.zeroX150.atomic.feature.command.CommandRegistry;
import me.zeroX150.atomic.feature.module.impl.external.ClientConfig;
import me.zeroX150.atomic.helper.Client;
import net.minecraft.text.*;

public class Help extends Command {

    public Help() {
        super("Help", "Shows all commands", "help", "h", "?", "cmds", "commands", "manual", "man");
    }

    @Override
    public void onExecute(String[] args) {
        Client.notifyUser("All commands and their description");
        for (Command command : CommandRegistry.getCommands()) {
            LiteralText t = new LiteralText(" - §a" + command.getName() + " §7(" + command.getAliases()[0] + "): §9" + command.getDescription());
            Style style = Style.EMPTY
                    .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, ClientConfig.chatPrefix.getValue() + command.getAliases()[0]))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.of("§a" + command.getName() + "\n§7(" + String.join(", ", command.getAliases()) + ")\n§9" + command.getDescription() + "\nClick to autofill")));
            t.setStyle(style);
            Atomic.client.player.sendMessage(t, false);
            //Client.notifyUser(" - "+command.getName()+" ("+command.getAliases()[0]+"): "+command.getDescription());
        }
    }
}
