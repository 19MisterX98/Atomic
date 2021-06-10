package me.zeroX150.atomic.feature.module.impl.movement;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import net.minecraft.client.util.math.MatrixStack;

public class AirJump extends Module {
    public AirJump() {
        super("AirJump", "Jumps in de air", ModuleType.MOVEMENT);
    }

    @Override
    public void tick() {
        if(Atomic.client.options.keyJump.isPressed()) {
            Atomic.client.player.setOnGround(true);
            Atomic.client.player.fallDistance = 0f;
        }
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
