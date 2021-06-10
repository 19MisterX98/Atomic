package me.zeroX150.atomic.feature.module.impl.render;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import net.minecraft.client.util.math.MatrixStack;

public class ClickGUI extends Module {
    public ClickGUI() {
        super("ClickGUI", "Opens the click gui", ModuleType.RENDER);
    }

    @Override
    public void tick() {
        Atomic.client.openScreen(new me.zeroX150.atomic.feature.gui.clickgui.ClickGUI());
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
        return "";
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }
}
