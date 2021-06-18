package me.zeroX150.atomic.feature.module.impl.world;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import net.minecraft.client.util.math.MatrixStack;

public class Fullbright extends Module {
    double og;

    public Fullbright() {
        super("Fullbright", "shine bright like a diamond", ModuleType.RENDER);
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {
        og = Atomic.client.options.gamma;
        Atomic.client.options.gamma = 10D;
    }

    @Override
    public void disable() {
        Atomic.client.options.gamma = og;
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

