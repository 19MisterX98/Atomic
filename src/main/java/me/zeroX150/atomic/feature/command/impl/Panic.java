package me.zeroX150.atomic.feature.command.impl;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.command.Command;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.helper.Client;

import java.util.ArrayList;
import java.util.List;

public class Panic extends Command {
    List<Module> stored = new ArrayList<>();

    public Panic() {
        super("Panic", "oh shit", "panic", "p", "disableall", "dall", "fuck");
    }

    @Override
    public void onExecute(String[] args) {
        if (args.length == 0) {
            stored.clear();
            Client.notifyUser("Disabling all non-render modules");
            Client.notifyUser("Specify \"hard\" to disable all modules and wipe chat");
            Client.notifyUser("Specify \"restore\" to restore all enabled modules before the panic");
            for (Module module : ModuleRegistry.getModules()) {
                if (module.getModuleType() != ModuleType.RENDER && module.isEnabled()) {
                    stored.add(module);
                    module.setEnabled(false);
                }
            }
        } else if (args[0].equalsIgnoreCase("hard")) {
            stored.clear();
            for (Module module : ModuleRegistry.getModules()) {
                if (module.isEnabled()) {
                    stored.add(module);
                    module.setEnabled(false);
                }
            }
            Atomic.client.inGameHud.getChatHud().clear(true);
        } else if (args[0].equalsIgnoreCase("restore")) {
            if (stored.size() == 0) {
                Client.notifyUser("The stored module list is empty");
            } else for (Module module : stored) {
                if (!module.isEnabled()) module.setEnabled(true);
            }
            stored.clear();
        }
    }
}
