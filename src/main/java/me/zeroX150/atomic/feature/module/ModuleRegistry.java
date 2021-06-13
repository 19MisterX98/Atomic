package me.zeroX150.atomic.feature.module;

import me.zeroX150.atomic.feature.module.impl.movement.*;
import me.zeroX150.atomic.feature.module.impl.render.ClickGUI;
import me.zeroX150.atomic.feature.module.impl.render.Hud;
import me.zeroX150.atomic.feature.module.impl.testing.TestModule;

import java.util.ArrayList;
import java.util.List;

public class ModuleRegistry {
    static List<Module> modules = new ArrayList<>();

    static {
        modules.add(new TestModule());
        modules.add(new ClickGUI());
        modules.add(new AirJump());
        modules.add(new ArrowJuke());
        modules.add(new AutoSneak());
        modules.add(new Blink());
        modules.add(new EntityFly());
        modules.add(new Hud());
        modules.add(new Boost());
        modules.add(new Flight());
        modules.add(new Jesus());
        modules.add(new MoonGravity());
        modules.add(new NoFall());
        modules.add(new NoJumpCooldown());
        modules.add(new ClickTP());
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
