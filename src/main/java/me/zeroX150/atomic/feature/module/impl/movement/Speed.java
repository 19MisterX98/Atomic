package me.zeroX150.atomic.feature.module.impl.movement;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.MultiValue;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;

public class Speed extends Module {
    MultiValue mode = (MultiValue) this.config.create("Mode", "Vanilla", "Vanilla", "BHop", "Legit", "Minihop").description("The mode");
    SliderValue newSpeed = (SliderValue) this.config.create("Speed multiplier", 1, 0, 5, 1).description("The speed multiplier");
    SliderValue bhopDown = (SliderValue) this.config.create("Down velocity", 1, -0.7, 3, 2).description("How fast to go down on bhop");

    double prev = 0.1;

    public Speed() {
        super("Speed", "schbeed", ModuleType.MOVEMENT);
        bhopDown.showOnlyIf(() -> mode.getValue().equalsIgnoreCase("bhop"));
        newSpeed.showOnlyIf(() -> !mode.getValue().equalsIgnoreCase("legit"));
    }

    @Override
    public void tick() {
        if (Atomic.client.player == null || Atomic.client.getNetworkHandler() == null) return;
        EntityAttributeInstance speed = Atomic.client.player.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (speed == null) return;
        switch (mode.getValue().toLowerCase()) {
            case "vanilla" -> speed.setBaseValue(prev * newSpeed.getValue());
            case "bhop" -> {
                speed.setBaseValue(prev * newSpeed.getValue());
                if (isMoving()) {
                    if (Atomic.client.player.isOnGround()) {
                        Atomic.client.player.jump();
                    } else {
                        Atomic.client.player.addVelocity(0, -bhopDown.getValue() / 10, 0);
                    }
                }
            }
            case "minihop" -> {
                speed.setBaseValue(prev * newSpeed.getValue());
                if (isMoving()) {
                    if (Atomic.client.player.isOnGround()) {
                        Atomic.client.player.jump();
                        Atomic.client.player.addVelocity(0, -.35, 0);
                    } else {
                        Atomic.client.player.setVelocity(Atomic.client.player.getVelocity().multiply(1.1));
                    }
                }
            }
            case "legit" -> {
                if (Atomic.client.player.isOnGround() && isMoving()) Atomic.client.player.jump();
                EntityAttributeInstance eai = Atomic.client.player.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
                if (eai != null) eai.setBaseValue(prev);
            }
        }
    }

    boolean isMoving() {
        GameOptions go = Atomic.client.options;
        return go.keyForward.isPressed() || go.keyBack.isPressed() || go.keyRight.isPressed() || go.keyLeft.isPressed();
    }

    @Override
    public void enable() {
        if (Atomic.client.player == null || Atomic.client.getNetworkHandler() == null) return;
        EntityAttributeInstance eai = Atomic.client.player.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (eai != null) prev = eai.getValue();
    }

    @Override
    public void disable() {
        if (Atomic.client.player == null || Atomic.client.getNetworkHandler() == null) return;
        EntityAttributeInstance eai = Atomic.client.player.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (eai != null) eai.setBaseValue(prev);
    }

    @Override
    public String getContext() {
        return mode.getValue();
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }
}

