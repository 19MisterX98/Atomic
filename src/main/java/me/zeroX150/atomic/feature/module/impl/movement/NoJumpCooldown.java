package me.zeroX150.atomic.feature.module.impl.movement;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.mixin.game.entity.LivingEntityAccessor;
import net.minecraft.client.util.math.MatrixStack;

public class NoJumpCooldown extends Module {
    public NoJumpCooldown() {
        super("NoJumpCooldown", "removes gay shit", ModuleType.MOVEMENT);
    }

    @Override
    public void tick() {
        ((LivingEntityAccessor) Atomic.client.player).setJumpingCooldown(0);
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

