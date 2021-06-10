package me.zeroX150.atomic.feature.command.impl;

import me.zeroX150.atomic.feature.command.Command;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.helper.Client;

public class Toggle extends Command {
    public Toggle() {
        super("Toggle", "toggles a module", "toggle", "t");
    }

    @Override
    public void onExecute(String[] args) {
        if (args.length == 0) {
            Client.notifyUser("ima need the module name");
            return;
        }
        Module m = ModuleRegistry.getByName(args[0].toLowerCase());
        if (m == null) {
            Client.notifyUser("Module not found bruh");
        } else m.toggle();
    }
}
