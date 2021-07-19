package me.zeroX150.atomic.feature.module.impl.render;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.BooleanValue;
import me.zeroX150.atomic.feature.module.config.MultiValue;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import net.minecraft.client.util.math.MatrixStack;

public class ClickGUI extends Module {
    public static SliderValue logoSize;
    public static SliderValue smooth;
    public static SliderValue dragFactor;
    public static BooleanValue enableTails;
    public static SliderValue tailSize;

    public static MultiValue theme;

    public ClickGUI() {
        super("ClickGUI", "Opens the click gui", ModuleType.RENDER);
        logoSize = (SliderValue) this.config.create("Logo size", 0.28, 0, 1, 2).description("The size of the logo at the top (0 to disable)");
        smooth = (SliderValue) this.config.create("Movement smooth", 10, 10, 30, 1).description("The factor to smooth movements of the tabs by");
        dragFactor = (SliderValue) this.config.create("Drag factor", 0.5, 0, 3, 1).description("The factor to rotate the tab by when you move it (0 to disable)");
        enableTails = (BooleanValue) this.config.create("Enable tails", false).description("Whether or not to enable tails");
        tailSize = (SliderValue) this.config.create("Tail size", 30, 10, 200, 0).description("The length of the tails");
        theme = (MultiValue) this.config.create("Theme", "Atomic", "Atomic", "Dark", "Walmart Sigma").description("The theme of the clickgui");
        tailSize.showOnlyIf(() -> enableTails.getValue());
        this.config.get("Keybind").setValue(344);
    }

    @Override
    public void tick() {
        if (!(Atomic.client.currentScreen instanceof me.zeroX150.atomic.feature.gui.clickgui.ClickGUI)) {
            if (me.zeroX150.atomic.feature.gui.clickgui.ClickGUI.INSTANCE == null)
                new me.zeroX150.atomic.feature.gui.clickgui.ClickGUI();
            Atomic.client.openScreen(me.zeroX150.atomic.feature.gui.clickgui.ClickGUI.INSTANCE);
        } else toggle();
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
