package me.zeroX150.atomic.feature.module.impl.render;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import net.minecraft.client.util.math.MatrixStack;

public class ClickGUI extends Module {
    public ClickGUI() {
        super("ClickGUI", "Opens the click gui", ModuleType.RENDER);
        this.config.get("Keybind").setValue(344);
    }

    @Override
    public void tick() {
        if (me.zeroX150.atomic.feature.gui.clickgui.ClickGUI.INSTANCE == null)
            new me.zeroX150.atomic.feature.gui.clickgui.ClickGUI();
        Atomic.client.openScreen(me.zeroX150.atomic.feature.gui.clickgui.ClickGUI.INSTANCE);
        toggle();
    }

    @Override
    public void enable() {
    }

    @Override
    public void disable() {

    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }
}
