package me.zeroX150.atomic.feature.command.impl;

import baritone.BaritoneProvider;
import baritone.api.BaritoneAPI;
import baritone.api.Settings;
import baritone.api.utils.SettingsUtil;
import me.zeroX150.atomic.feature.command.Command;
import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.feature.module.impl.render.OreSim.OreSim;
import me.zeroX150.atomic.helper.Client;
import net.fabricmc.loader.api.FabricLoader;

public class BaritoneOreSim extends Command {

    public BaritoneOreSim() {
        super("OreSimAutomine", "Start and stop baritone", "automine");
    }

    @Override
    public void onExecute(String[] args) {
        if (FabricLoader.getInstance().getModContainer("baritone").isEmpty()) {
            Client.notifyUser("Baritone is not installed ");
            return;
        }
        OreSim oreSim = (OreSim) ModuleRegistry.getByClass(OreSim.class);
        if (!oreSim.isEnabled()) {
            Client.notifyUser("You need to have oresim enabled for this");
            return;
        }
        if (args.length == 1) {
            String command = args[0].toLowerCase();
            if (command.equals("start")) {
                System.out.println("starting");
                oreSim.automine = true;
                return;
            } else if (command.equals("stop")) {
                System.out.println("stopping");
                oreSim.automine = false;
                return;
            }
        }
        Client.notifyUser("Syntax: .automine <start/stop>");
    }
}
