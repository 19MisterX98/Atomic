package me.zeroX150.atomic.feature.command.impl;

import me.zeroX150.atomic.feature.command.Command;
import me.zeroX150.atomic.feature.gui.clickgui.ClickGUI;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.feature.module.config.DynamicValue;
import me.zeroX150.atomic.helper.Client;
import me.zeroX150.atomic.helper.TypeConverter;

public class Config extends Command {
    public Config() {
        super("Config", "le poggers", "config", "conf");
    }

    @Override
    public void onExecute(String[] args) {
        if (args.length == 0) {
            Client.notifyUser("Syntax: .config (module) <key> <value>");
            Client.notifyUser("For a key with spaces, use - as a seperator");
            Client.notifyUser("Example: \".config blockspammer times-per-tick 11\" to set the \"times per tick\" property to 11");
            return;
        }
        Module target = ModuleRegistry.getByName(args[0]);
        if (target == null) {
            Client.notifyUser("Module not found");
            return;
        }
        if (args.length == 1) {
            for (DynamicValue<?> dynamicValue : target.config.getAll()) {
                Client.notifyUser(dynamicValue.getKey() + " is a " + dynamicValue.getType().getName() + " and has value " + dynamicValue.getValue());
            }
        } else if (args.length == 2) {
            DynamicValue<?> val = target.config.get(args[1].replaceAll("-", " ").toLowerCase());
            if (val == null) {
                Client.notifyUser("Key not found");
                return;
            }
            Client.notifyUser(val.getKey() + " = " + val.getValue());
        } else if (args.length == 3) {
            DynamicValue<?> val = target.config.get(args[1].replaceAll("-", " ").toLowerCase());
            if (val == null) {
                Client.notifyUser("Key not found");
                return;
            }
            Object newValue = TypeConverter.convert(args[2], val.getType());
            if (newValue == null) {
                Client.notifyUser("Cannot set value. Check if you inputted everything correctly.");
                return;
            }
            val.setValue(newValue);
            if (ClickGUI.INSTANCE != null) {
                if (ClickGUI.INSTANCE.currentConfig != null && ClickGUI.INSTANCE.currentConfig.parent == target) {
                    ClickGUI.INSTANCE.showModuleConfig(target);
                }
            }
        }
    }
}
