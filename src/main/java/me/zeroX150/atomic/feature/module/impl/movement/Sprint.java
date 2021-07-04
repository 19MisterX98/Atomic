package me.zeroX150.atomic.feature.module.impl.movement;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import net.minecraft.client.util.math.MatrixStack;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", "togglesprint for jewish people", ModuleType.MOVEMENT);
    }

    @Override
    public void tick() {
        if (Atomic.client.player == null || Atomic.client.getNetworkHandler() == null) return;
        if (Atomic.client.options.keyForward.isPressed() && !Atomic.client.options.keyBack.isPressed()
                && !Atomic.client.player.isSneaking() && !Atomic.client.player.horizontalCollision) {
            Atomic.client.player.setSprinting(true);
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

