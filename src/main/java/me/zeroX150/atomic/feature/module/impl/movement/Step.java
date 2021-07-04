package me.zeroX150.atomic.feature.module.impl.movement;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import net.minecraft.client.util.math.MatrixStack;

public class Step extends Module {
    SliderValue height = (SliderValue) this.config.create("Step height", 3, 1, 50, 0).description("How high to step");

    public Step() {
        super("Step", "spider but fast", ModuleType.MOVEMENT);
    }

    @Override
    public void tick() {
        if (Atomic.client.player == null || Atomic.client.getNetworkHandler() == null) return;
        Atomic.client.player.stepHeight = (float) (height.getValue() + 0);
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {
        if (Atomic.client.player == null || Atomic.client.getNetworkHandler() == null) return;
        Atomic.client.player.stepHeight = 0.6f;
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

