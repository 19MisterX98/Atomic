package me.zeroX150.atomic.feature.module;

import me.zeroX150.atomic.feature.gui.notifications.Notification;
import me.zeroX150.atomic.feature.module.config.ModuleConfig;
import net.minecraft.client.util.math.MatrixStack;

public abstract class Module {
    private final String name;
    private final String description;
    private final ModuleType moduleType;
    public ModuleConfig config;
    private boolean enabled = false;

    public Module(String n, String d, ModuleType type) {
        this.name = n;
        this.description = d;
        this.moduleType = type;
        this.config = new ModuleConfig();
        this.config.create("Keybind", -1);
    }

    public ModuleType getModuleType() {
        return moduleType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public abstract void tick();

    public abstract void enable();

    public abstract void disable();

    public abstract String getContext();

    public abstract void onWorldRender(MatrixStack matrices);

    public abstract void onHudRender();

    public void onFastTick() {

    }

    public void toggle() {
        setEnabled(!enabled);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        Notification.create(1000, "Module toggle", (this.enabled ? "§aEn" : "§cDis") + "abled §r" + this.getName());
        if (this.enabled) this.enable();
        else this.disable();
    }

}
