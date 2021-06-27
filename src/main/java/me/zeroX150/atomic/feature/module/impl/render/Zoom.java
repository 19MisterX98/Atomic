package me.zeroX150.atomic.feature.module.impl.render;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.BooleanValue;
import me.zeroX150.atomic.feature.module.config.DynamicValue;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import me.zeroX150.atomic.helper.Renderer;
import me.zeroX150.atomic.helper.keybind.Keybind;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class Zoom extends Module {
    static long enabledTime = 0;
    static SliderValue finalFov;
    Keybind kb;
    double msens = 0.5d;
    BooleanValue hold = this.config.create("Hold", true);

    public Zoom() {
        super("Zoom", "ok zoomer", ModuleType.RENDER);
        finalFov = this.config.create("Fov", 30, 1, 180, 0);
    }

    public static double getZoomValue(double vanilla) {
        long enabledFor = System.currentTimeMillis() - enabledTime;
        double prog = MathHelper.clamp(enabledFor / 100d, 0, 1);
        if (!ModuleRegistry.getByClass(Zoom.class).isEnabled()) prog = Math.abs(1 - prog);
        prog = easeOutBounce(prog);
        return Renderer.lerp(vanilla, finalFov.getValue(), prog);
    }

    static double easeOutBounce(double x) {
        return x < 0.5 ? 4 * x * x * x : 1 - Math.pow(-2 * x + 2, 3) / 2;
    }

    @Override
    public void tick() {
        if (kb == null) return;
        if (!kb.isHeld() && hold.getValue()) this.setEnabled(false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void enable() {
        msens = Atomic.client.options.mouseSensitivity;
        Atomic.client.options.mouseSensitivity = msens * (finalFov.getValue() / Atomic.client.options.fov);
        // retard the keybind thing is always an int shut the fuck up
        kb = new Keybind(((DynamicValue<Integer>) this.config.get("Keybind")).getValue());
        enabledTime = System.currentTimeMillis();
    }

    @Override
    public void disable() {
        enabledTime = System.currentTimeMillis();
        Atomic.client.options.mouseSensitivity = msens;
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

