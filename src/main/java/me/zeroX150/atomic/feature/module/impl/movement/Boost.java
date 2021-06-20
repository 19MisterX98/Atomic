package me.zeroX150.atomic.feature.module.impl.movement;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.MultiValue;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public class Boost extends Module {
    SliderValue strength = this.config.create("Strength", 3, 0.1, 10, 1);
    MultiValue mode = this.config.create("Mode", "add", "add", "overwrite");

    public Boost() {
        super("Boost", "weee", ModuleType.MOVEMENT);
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {
        if (Atomic.client.player == null || Atomic.client.getNetworkHandler() == null) return;
        setEnabled(false);
        Vec3d newVelocity = Atomic.client.player.getRotationVector().multiply(strength.getValue());
        if (this.mode.getValue().equalsIgnoreCase("add"))
            Atomic.client.player.addVelocity(newVelocity.x, newVelocity.y, newVelocity.z);
        else Atomic.client.player.setVelocity(newVelocity);
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
