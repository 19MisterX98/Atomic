package me.zeroX150.atomic.feature.module;

import me.zeroX150.atomic.feature.module.impl.render.ClickGUI;
import me.zeroX150.atomic.feature.module.impl.testing.TestModule;
import me.zeroX150.atomic.feature.module.impl.movement.AirJump;
import me.zeroX150.atomic.feature.module.impl.movement.ArrowJuke;

import java.util.ArrayList;
import java.util.List;

public class ModuleRegistry {
    static List<Module> modules = new ArrayList<>();

    static {
        modules.add(new TestModule());
        modules.add(new ClickGUI());
        modules.add(new AirJump());
        modules.add(new ArrowJuke());
    }

    public static List<Module> getModules() {
        return modules;
    }

    public static Module getByClass(Class<? extends Module> clazz) {
        for (Module module : getModules()) {
            if (module.getClass() == clazz) return module;
        }
        return null;
    }

    public static Module getByName(String n) {
        for (Module module : getModules()) {
            if (module.getName().equalsIgnoreCase(n)) return module;
        }
        return null;
    }
}
