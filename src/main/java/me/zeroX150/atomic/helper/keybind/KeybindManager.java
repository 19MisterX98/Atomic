package me.zeroX150.atomic.helper.keybind;

import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleRegistry;

import java.util.HashMap;
import java.util.Map;

public class KeybindManager {
    public static Map<Module, Keybind> keybindMap = new HashMap<>();

    public static void init() {
        for (Module module : ModuleRegistry.getModules()) {
            if (!module.config.get("Keybind").getValue().equals(-1.0)) {
                keybindMap.put(module, new Keybind(Integer.parseInt(module.config.get("Keybind").getValue() + "")));
            }
        }
    }

    public static void update() {
        for (Module module : keybindMap.keySet().toArray(new Module[0])) {
            Keybind kb = keybindMap.get(module);
            if (kb.isPressed()) module.toggle();
        }
    }

    public static void reload() {
        keybindMap.clear();
        init();
    }
}
