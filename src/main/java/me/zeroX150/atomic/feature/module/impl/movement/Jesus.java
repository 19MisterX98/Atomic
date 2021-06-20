package me.zeroX150.atomic.feature.module.impl.movement;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.MultiValue;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import me.zeroX150.atomic.mixin.game.entity.LivingEntityMixin;
import net.minecraft.client.util.math.MatrixStack;

/**
 * @see LivingEntityMixin
 */
public class Jesus extends Module {
    public static MultiValue mode;

    SliderValue velStrength = this.config.create("Velocity strength", 0.1, 0.001, 0.3, 3);

    public Jesus() {
        super("Jesus", "No description", ModuleType.MOVEMENT);
        mode = this.config.create("Mode", "solid", "jump", "velocity", "solid", "legit");
        velStrength.showOnlyIf(() -> Jesus.mode.getValue().equalsIgnoreCase("velocity"));
    }

    @Override
    public void tick() {
        if (Atomic.client.player == null || Atomic.client.getNetworkHandler() == null) return;
        if (Atomic.client.player.isWet()) {
            switch (mode.getValue().toLowerCase()) {
                case "jump" -> Atomic.client.player.jump();
                case "velocity" -> Atomic.client.player.setVelocity(Atomic.client.player.getVelocity().x, velStrength.getValue(), Atomic.client.player.getVelocity().z);
                case "legit" -> Atomic.client.player.addVelocity(0, 0.03999999910593033, 0); // LivingEntity:1978, vanilla velocity
            }
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

