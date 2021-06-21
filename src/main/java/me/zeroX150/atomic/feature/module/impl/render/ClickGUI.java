package me.zeroX150.atomic.feature.module.impl.render;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import net.minecraft.client.util.math.MatrixStack;

public class ClickGUI extends Module {
    public static SliderValue logoSize;
    public static SliderValue smooth;
    public static SliderValue dragFactor;

    public ClickGUI() {
        super("ClickGUI", "Opens the click gui", ModuleType.RENDER);
        logoSize = this.config.create("Logo size", 0.28, 0, 1, 2);
        smooth = this.config.create("Movement smooth", 20, 10, 30, 1);
        dragFactor = this.config.create("Drag factor", 1.5, 0, 20, 1);
        this.config.get("Keybind").setValue(344);
    }

    @Override
    public void tick() {
    }

    @Override
    public void enable() {
        if (me.zeroX150.atomic.feature.gui.clickgui.ClickGUI.INSTANCE == null)
            new me.zeroX150.atomic.feature.gui.clickgui.ClickGUI();
        Atomic.client.openScreen(me.zeroX150.atomic.feature.gui.clickgui.ClickGUI.INSTANCE);
        toggle();
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
